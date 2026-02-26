package com.nhs.scheduler.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UnscheduledSession {
    @JsonProperty("session")
    @JsonAlias("job")
    private Session session;
    private String reason;

    public UnscheduledSession() {
    }

    public UnscheduledSession(Session session, String reason) {
        this.session = session;
        this.reason = reason;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
