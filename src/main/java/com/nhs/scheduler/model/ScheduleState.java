package com.nhs.scheduler.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;

import java.util.ArrayList;
import java.util.List;

public class ScheduleState {
    @Valid
    private List<Room> rooms = new ArrayList<>();

    @Valid
    private List<Employee> employees = new ArrayList<>();

    @Valid
    @JsonProperty("sessions")
    @JsonAlias("jobs")
    private List<Session> sessions = new ArrayList<>();
    private List<String> purposeOptions = new ArrayList<>();
    private List<String> skillOptions = new ArrayList<>();
    private ScheduleResult schedule;

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
