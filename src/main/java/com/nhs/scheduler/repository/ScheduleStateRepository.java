package com.nhs.scheduler.repository;

import com.nhs.scheduler.model.ScheduleState;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ScheduleStateRepository extends MongoRepository<ScheduleState, String> {
}
