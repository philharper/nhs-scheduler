package com.nhs.scheduler.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class ScheduleResult {
    private List<Assignment> assignments = new ArrayList<>();
    @JsonProperty("unscheduledSessions")
    @JsonAlias("unscheduledJobs")
    private List<UnscheduledSession> unscheduledSessions = new ArrayList<>();

    public List<Assignment> getAssignments() {
        return assignments;
    }

    public void setAssignments(List<Assignment> assignments) {
        this.assignments = assignments;
    }

    public List<UnscheduledSession> getUnscheduledSessions() {
        return unscheduledSessions;
    }

    public void setUnscheduledSessions(List<UnscheduledSession> unscheduledSessions) {
        this.unscheduledSessions = unscheduledSessions;
    }
}
