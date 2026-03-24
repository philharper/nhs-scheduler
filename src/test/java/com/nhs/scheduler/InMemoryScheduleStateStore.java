package com.nhs.scheduler;

import com.nhs.scheduler.model.ScheduleState;
import com.nhs.scheduler.service.ScheduleStateStore;

class InMemoryScheduleStateStore implements ScheduleStateStore {
    private ScheduleState state = new ScheduleState();

    @Override
    public ScheduleState read() {
        return state;
    }

    @Override
    public void write(ScheduleState state) {
        this.state = state;
    }

    @Override
    public boolean hasPersistedState() {
        return true;
    }

    @Override
    public String getStorageDescription() {
        return "InMemory";
    }
}
