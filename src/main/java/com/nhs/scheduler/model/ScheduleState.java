package com.nhs.scheduler.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Document("schedule_state")
public class ScheduleState {
    public static final String SINGLETON_ID = "primary";

    @Id
    private String id = SINGLETON_ID;

    @Valid
    private List<Room> rooms = new ArrayList<>();

    @Valid
    private List<Employee> employees = new ArrayList<>();

    @Valid
    @JsonProperty("sessions")
    @JsonAlias("jobs")
    private List<Session> sessions = new ArrayList<>();
    private LocalDate scheduleWeekStart;
    private Map<String, ScheduleResult> schedulesByWeek = new LinkedHashMap<>();
    private List<String> purposeOptions = new ArrayList<>();
    private List<String> skillOptions = new ArrayList<>();
    private ScheduleResult schedule;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Room> getRooms() {
        return rooms;
    }

    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }

    public List<Session> getSessions() {
        return sessions;
    }

    public void setSessions(List<Session> sessions) {
        this.sessions = sessions;
    }

    public LocalDate getScheduleWeekStart() {
        return scheduleWeekStart;
    }

    public void setScheduleWeekStart(LocalDate scheduleWeekStart) {
        this.scheduleWeekStart = scheduleWeekStart;
    }

    public Map<String, ScheduleResult> getSchedulesByWeek() {
        return schedulesByWeek;
    }

    public void setSchedulesByWeek(Map<String, ScheduleResult> schedulesByWeek) {
        this.schedulesByWeek = schedulesByWeek == null ? new LinkedHashMap<>() : schedulesByWeek;
    }

    public List<String> getPurposeOptions() {
        return purposeOptions;
    }

    public void setPurposeOptions(List<String> purposeOptions) {
        this.purposeOptions = purposeOptions;
    }

    public List<String> getSkillOptions() {
        return skillOptions;
    }

    public void setSkillOptions(List<String> skillOptions) {
        this.skillOptions = skillOptions;
    }

    public ScheduleResult getSchedule() {
        return schedule;
    }

    public void setSchedule(ScheduleResult schedule) {
        this.schedule = schedule;
    }
}
