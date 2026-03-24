package com.nhs.scheduler.model;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class AnnualLeaveEntry {
    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;

    public AnnualLeaveEntry() {
    }

    public AnnualLeaveEntry(LocalDate startDate, LocalDate endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
}
