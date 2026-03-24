package com.nhs.scheduler;

import com.nhs.scheduler.model.AvailabilityWindow;
import com.nhs.scheduler.model.Employee;
import com.nhs.scheduler.model.Room;
import com.nhs.scheduler.model.ScheduleResult;
import com.nhs.scheduler.model.ScheduleState;
import com.nhs.scheduler.model.Session;
import com.nhs.scheduler.service.ScheduleService;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ScheduleServiceWeekSelectionTest {

    @Test
    void schedulesArePersistedSeparatelyByWeek() {
        InMemoryScheduleStateStore store = new InMemoryScheduleStateStore();
        ScheduleService scheduleService = new ScheduleService(store);

        ScheduleState state = new ScheduleState();
        state.setRooms(List.of(new Room("R1", "ECG Lab", "ecg")));
        state.setEmployees(List.of(employee("E1", "Alice")));
        state.setSessions(List.of(session("S1", "ECG Clinic")));
        scheduleService.replaceState(state);

        LocalDate weekOne = LocalDate.of(2026, 3, 23);
        scheduleService.updateScheduleWeek(weekOne);
        ScheduleResult resultOne = scheduleService.generateSchedule();

        LocalDate weekTwo = LocalDate.of(2026, 3, 30);
        scheduleService.updateScheduleWeek(weekTwo);
        ScheduleResult initiallyEmptyWeekTwo = scheduleService.getSchedule();
        ScheduleResult resultTwo = scheduleService.generateSchedule();

        scheduleService.updateScheduleWeek(weekOne);
        ScheduleResult reloadedWeekOne = scheduleService.getSchedule();

        assertEquals(1, resultOne.getAssignments().size());
        assertEquals(0, initiallyEmptyWeekTwo.getAssignments().size());
        assertEquals(1, resultTwo.getAssignments().size());
        assertEquals(1, reloadedWeekOne.getAssignments().size());
        assertEquals("S1", reloadedWeekOne.getAssignments().getFirst().getSession().getId());
    }

    private Employee employee(String id, String name) {
        Employee employee = new Employee();
        employee.setId(id);
        employee.setName(name);
        employee.setSkills(Set.of("ecg"));
        employee.setAvailability(List.of(new AvailabilityWindow(DayOfWeek.MONDAY, LocalTime.of(8, 0), LocalTime.of(17, 0))));
        return employee;
    }

    private Session session(String id, String name) {
        Session session = new Session();
        session.setId(id);
        session.setName(name);
        session.setRequiredSkill("ecg");
        session.setPurpose("ecg");
        session.setDayOfWeek(DayOfWeek.MONDAY);
        session.setStart(LocalTime.of(8, 0));
        session.setEnd(LocalTime.of(12, 0));
        return session;
    }
}
