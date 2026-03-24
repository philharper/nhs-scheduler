package com.nhs.scheduler.model;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class ScheduleWeekSelectionRequest {
    @NotNull
    private LocalDate scheduleWeekStart;

    public LocalDate getScheduleWeekStart() {
        return scheduleWeekStart;
    }

    public void setScheduleWeekStart(LocalDate scheduleWeekStart) {
        this.scheduleWeekStart = scheduleWeekStart;
    }
}
