package com.nhs.scheduler.model;

import jakarta.validation.constraints.NotBlank;

public class Room {
    @NotBlank
    private String id;

    @NotBlank
    private String name;

    @NotBlank
    private String purpose;

    public Room() {
    }

    public Room(String id, String name, String purpose) {
        this.id = id;
        this.name = name;
        this.purpose = purpose;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }
}
