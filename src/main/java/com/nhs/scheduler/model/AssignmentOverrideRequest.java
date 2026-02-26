package com.nhs.scheduler.model;

import jakarta.validation.constraints.NotBlank;

public class AssignmentOverrideRequest {
    @NotBlank
    private String sessionId;

    @NotBlank
    private String employeeId;

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }
}
