package com.nhs.scheduler.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Assignment {
    @JsonProperty("session")
    @JsonAlias("job")
    private Session session;
    private Employee employee;
    private Room room;

    public Assignment() {
    }

    public Assignment(Session session, Employee employee, Room room) {
        this.session = session;
        this.employee = employee;
        this.room = room;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }
}
