package com.nhs.scheduler.service;

import com.nhs.scheduler.model.ScheduleState;

public interface ScheduleStateStore {
    ScheduleState read();

    void write(ScheduleState state);

    boolean hasPersistedState();

    String getStorageDescription();
}
