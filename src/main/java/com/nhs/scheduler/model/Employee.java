package com.nhs.scheduler.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Employee {
    @NotBlank
    private String id;

    @NotBlank
    private String name;

    private Set<String> skills = new HashSet<>();

    @Valid
    private List<AvailabilityWindow> availability = new ArrayList<>();

    public Employee() {
    }

    public Employee(String id, String name, Set<String> skills, List<AvailabilityWindow> availability) {
        this.id = id;
        this.name = name;
        this.skills = skills;
        this.availability = availability;
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

    public Set<String> getSkills() {
        return skills;
    }

    public void setSkills(Set<String> skills) {
        this.skills = skills;
    }

    public List<AvailabilityWindow> getAvailability() {
        return availability;
    }

    public void setAvailability(List<AvailabilityWindow> availability) {
        this.availability = availability;
    }
}
