package com.nhs.scheduler.service;

import com.nhs.scheduler.model.ScheduleState;
import com.nhs.scheduler.repository.ScheduleStateRepository;
import org.springframework.stereotype.Service;

@Service
public class MongoScheduleStateStore implements ScheduleStateStore {
    private final ScheduleStateRepository repository;

    public MongoScheduleStateStore(ScheduleStateRepository repository) {
        this.repository = repository;
    }

    @Override
    public ScheduleState read() {
        return repository.findById(ScheduleState.SINGLETON_ID).orElseGet(this::defaultState);
    }

    @Override
    public void write(ScheduleState state) {
        state.setId(ScheduleState.SINGLETON_ID);
        repository.save(state);
    }

    @Override
    public boolean hasPersistedState() {
        return repository.existsById(ScheduleState.SINGLETON_ID);
    }

    @Override
    public String getStorageDescription() {
        return "MongoDB";
    }

    private ScheduleState defaultState() {
        ScheduleState state = new ScheduleState();
        state.setId(ScheduleState.SINGLETON_ID);
        return state;
    }
}
