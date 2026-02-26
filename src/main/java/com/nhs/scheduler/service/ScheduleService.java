package com.nhs.scheduler.service;

import com.nhs.scheduler.model.Assignment;
import com.nhs.scheduler.model.AvailabilityWindow;
import com.nhs.scheduler.model.Employee;
import com.nhs.scheduler.model.Room;
import com.nhs.scheduler.model.ScheduleResult;
import com.nhs.scheduler.model.ScheduleState;
import com.nhs.scheduler.model.Session;
import com.nhs.scheduler.model.UnscheduledSession;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class ScheduleService {
    private static final long HALF_DAY_MINUTES = 240;

    private final FileStateStore fileStateStore;

    public ScheduleService(FileStateStore fileStateStore) {
        this.fileStateStore = fileStateStore;
    }

    public ScheduleState getState() {
        return fileStateStore.read();
    }

    public ScheduleState replaceState(ScheduleState state) {
        validateState(state);
        ScheduleState existing = getState();
        if (state.getSchedule() == null) {
            state.setSchedule(existing.getSchedule());
        }
        fileStateStore.write(state);
        return state;
    }

    public ScheduleResult getSchedule() {
        ScheduleState state = getState();
        return state.getSchedule() == null ? new ScheduleResult() : state.getSchedule();
    }

    public ScheduleResult generateSchedule() {
        ScheduleState state = getState();
        validateState(state);

        List<Session> sortedSessions = new ArrayList<>(state.getSessions());
        sortedSessions.sort(Comparator
                .comparing(Session::getDayOfWeek)
                .thenComparing(Session::getStart)
                .thenComparing(Session::getEnd)
                .thenComparing(Session::getId));

        Map<String, List<Session>> employeeAssignments = new HashMap<>();
        Map<String, List<Session>> roomAssignments = new HashMap<>();
        for (Employee employee : state.getEmployees()) {
            employeeAssignments.put(employee.getId(), new ArrayList<>());
        }

        ScheduleResult result = new ScheduleResult();

        for (Session session : sortedSessions) {
            List<Room> compatibleRooms = state.getRooms().stream()
                    .filter(room -> room.getPurpose().equalsIgnoreCase(session.getPurpose()))
                    .toList();

            if (compatibleRooms.isEmpty()) {
                result.getUnscheduledSessions().add(new UnscheduledSession(session, "No room exists for required purpose"));
                continue;
            }

            Room selectedRoom = chooseRoom(compatibleRooms, roomAssignments, session);
            if (selectedRoom == null) {
                result.getUnscheduledSessions().add(new UnscheduledSession(session, "No room available at this time"));
                continue;
            }

            Employee selected = chooseEmployee(state.getEmployees(), employeeAssignments, session);
            if (selected == null) {
                result.getUnscheduledSessions().add(new UnscheduledSession(session, "No qualified employee available"));
                continue;
            }

            List<Session> roomSessions = roomAssignments.computeIfAbsent(selectedRoom.getId(), k -> new ArrayList<>());
            employeeAssignments.get(selected.getId()).add(session);
            roomSessions.add(session);
            result.getAssignments().add(new Assignment(session, selected, selectedRoom));
        }

        state.setSchedule(result);
        fileStateStore.write(state);

        return result;
    }

    public String getDataFilePath() {
        return fileStateStore.getFilePath();
    }

    private Employee chooseEmployee(List<Employee> employees,
                                    Map<String, List<Session>> employeeAssignments,
                                    Session session) {
        return employees.stream()
                .filter(employee -> employee.getSkills().stream().anyMatch(s -> s.equalsIgnoreCase(session.getRequiredSkill())))
                .filter(employee -> isAvailable(employee, session))
                .filter(employee -> !hasConflict(employeeAssignments.getOrDefault(employee.getId(), List.of()), session))
                .min(Comparator.comparingInt(employee -> employeeAssignments.getOrDefault(employee.getId(), List.of()).size()))
                .orElse(null);
    }

    private Room chooseRoom(List<Room> rooms,
                            Map<String, List<Session>> roomAssignments,
                            Session session) {
        return rooms.stream()
                .filter(room -> !hasConflict(roomAssignments.getOrDefault(room.getId(), List.of()), session))
                .min(Comparator.comparingInt(room -> roomAssignments.getOrDefault(room.getId(), List.of()).size()))
                .orElse(null);
    }

    private boolean isAvailable(Employee employee, Session session) {
        return employee.getAvailability().stream()
                .anyMatch(window -> window.getDayOfWeek() == session.getDayOfWeek()
                        && !window.getStart().isAfter(session.getStart())
                        && !window.getEnd().isBefore(session.getEnd()));
    }

    private boolean hasConflict(List<Session> existingSessions, Session candidate) {
        return existingSessions.stream()
                .filter(session -> session.getDayOfWeek() == candidate.getDayOfWeek())
                .anyMatch(session -> overlaps(session.getStart(), session.getEnd(), candidate.getStart(), candidate.getEnd()));
    }

    private boolean overlaps(LocalTime aStart, LocalTime aEnd, LocalTime bStart, LocalTime bEnd) {
        return aStart.isBefore(bEnd) && bStart.isBefore(aEnd);
    }

    private void validateState(ScheduleState state) {
        if (state == null) {
            throw new IllegalArgumentException("State is required");
        }

        validateRooms(state.getRooms());
        validateEmployees(state.getEmployees());
        validateSessions(state.getSessions());
    }

    private void validateRooms(List<Room> rooms) {
        Set<String> ids = new HashSet<>();
        for (Room room : rooms) {
            if (isBlank(room.getId()) || isBlank(room.getName()) || isBlank(room.getPurpose())) {
                throw new IllegalArgumentException("Each room must have id, name, and purpose");
            }
            if (!ids.add(room.getId())) {
                throw new IllegalArgumentException("Duplicate room id: " + room.getId());
            }
        }
    }

    private void validateEmployees(List<Employee> employees) {
        Set<String> ids = new HashSet<>();
        for (Employee employee : employees) {
            if (isBlank(employee.getId()) || isBlank(employee.getName())) {
                throw new IllegalArgumentException("Each employee must have id and name");
            }
            if (!ids.add(employee.getId())) {
                throw new IllegalArgumentException("Duplicate employee id: " + employee.getId());
            }
            if (employee.getSkills() == null || employee.getSkills().isEmpty()) {
                throw new IllegalArgumentException("Employee " + employee.getId() + " must have at least one skill");
            }
            for (AvailabilityWindow window : employee.getAvailability()) {
                if (window.getDayOfWeek() == null || window.getStart() == null || window.getEnd() == null) {
                    throw new IllegalArgumentException("Employee " + employee.getId() + " has incomplete availability window");
                }
                if (!window.getStart().isBefore(window.getEnd())) {
                    throw new IllegalArgumentException("Employee " + employee.getId() + " has invalid availability window");
                }
            }
        }
    }

    private void validateSessions(List<Session> sessions) {
        Set<String> ids = new HashSet<>();
        for (Session session : sessions) {
            if (isBlank(session.getId()) || isBlank(session.getName())
                    || isBlank(session.getRequiredSkill()) || isBlank(session.getPurpose()) || session.getDayOfWeek() == null
                    || session.getStart() == null || session.getEnd() == null) {
                throw new IllegalArgumentException("Each session must have id, name, requiredSkill, purpose, dayOfWeek, start, and end");
            }
            if (!ids.add(session.getId())) {
                throw new IllegalArgumentException("Duplicate session id: " + session.getId());
            }
            if (!session.getStart().isBefore(session.getEnd())) {
                throw new IllegalArgumentException("Session " + session.getId() + " has invalid time range");
            }
            long minutes = Duration.between(session.getStart(), session.getEnd()).toMinutes();
            if (minutes != HALF_DAY_MINUTES) {
                throw new IllegalArgumentException("Session " + session.getId() + " must be exactly half-day (4 hours)");
            }
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
