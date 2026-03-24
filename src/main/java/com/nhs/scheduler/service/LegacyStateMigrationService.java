package com.nhs.scheduler.service;

import com.nhs.scheduler.model.ScheduleState;
import jakarta.annotation.PostConstruct;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty(name = "scheduler.legacy-migration.enabled", havingValue = "true", matchIfMissing = true)
public class LegacyStateMigrationService {
    private final ScheduleStateStore scheduleStateStore;
    private final LegacyFileStateLoader legacyFileStateLoader;

    public LegacyStateMigrationService(ScheduleStateStore scheduleStateStore, LegacyFileStateLoader legacyFileStateLoader) {
        this.scheduleStateStore = scheduleStateStore;
        this.legacyFileStateLoader = legacyFileStateLoader;
    }

    @PostConstruct
    public void migrateIfNeeded() {
        if (scheduleStateStore.hasPersistedState()) {
            return;
        }
        if (!legacyFileStateLoader.exists()) {
            return;
        }

        ScheduleState legacyState = legacyFileStateLoader.read();
        legacyState.setId(ScheduleState.SINGLETON_ID);
        scheduleStateStore.write(legacyState);
    }
}
