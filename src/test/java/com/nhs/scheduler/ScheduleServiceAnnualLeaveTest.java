package com.nhs.scheduler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhs.scheduler.config.JacksonConfig;
import com.nhs.scheduler.model.AnnualLeaveEntry;
import com.nhs.scheduler.model.AvailabilityWindow;
import com.nhs.scheduler.model.Employee;
import com.nhs.scheduler.model.Room;
import com.nhs.scheduler.model.ScheduleResult;
import com.nhs.scheduler.model.ScheduleState;
import com.nhs.scheduler.model.Session;
import com.nhs.scheduler.service.FileStateStore;
import com.nhs.scheduler.service.ScheduleService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ScheduleServiceAnnualLeaveTest {

    @TempDir
    Path tempDir;

    @Test
    void employeeOnAnnualLeaveIsNotScheduled() {
        ObjectMapper objectMapper = new JacksonConfig().objectMapper();
        FileStateStore store = new FileStateStore(objectMapper, tempDir.resolve("schedule-state.json").toString());
        store.init();
        ScheduleService scheduleService = new ScheduleService(store);

        ScheduleState state = new ScheduleState();
        state.setScheduleWeekStart(LocalDate.of(2026, 3, 23));
        state.setRooms(List.of(new Room("R1", "Echo Lab", "echo")));
        state.setEmployees(List.of(
                employee("E1", "On Leave", Set.of("echo"), List.of(new AnnualLeaveEntry(LocalDate.of(2026, 3, 23), LocalDate.of(2026, 3, 27)))),
                employee("E2", "Available", Set.of("echo"), List.of())
        ));
        state.setSessions(List.of(session("S1", "Echo Clinic", "echo", "echo", DayOfWeek.MONDAY, "08:00", "12:00")));

        scheduleService.replaceState(state);
        ScheduleResult result = scheduleService.generateSchedule();

        assertEquals(1, result.getAssignments().size());
        assertEquals("E2", result.getAssignments().getFirst().getEmployee().getId());
    }

    private Employee employee(String id, String name, Set<String> skills, List<AnnualLeaveEntry> annualLeave) {
        Employee employee = new Employee();
        employee.setId(id);
        employee.setName(name);
        employee.setSkills(skills);
        employee.setAvailability(List.of(new AvailabilityWindow(DayOfWeek.MONDAY, LocalTime.of(8, 0), LocalTime.of(17, 0))));
        employee.setAnnualLeave(annualLeave);
        return employee;
    }

    private Session session(String id, String name, String requiredSkill, String purpose, DayOfWeek dayOfWeek, String start, String end) {
        Session session = new Session();
        session.setId(id);
        session.setName(name);
        session.setRequiredSkill(requiredSkill);
        session.setPurpose(purpose);
        session.setDayOfWeek(dayOfWeek);
        session.setStart(LocalTime.parse(start + ":00"));
        session.setEnd(LocalTime.parse(end + ":00"));
        return session;
    }
}
