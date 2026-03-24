package com.nhs.scheduler.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhs.scheduler.model.ScheduleState;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Component
public class LegacyFileStateLoader {
    private final ObjectMapper objectMapper;
    private final Path filePath;

    public LegacyFileStateLoader(ObjectMapper objectMapper,
                                 @Value("${scheduler.data-file:data/schedule-state.json}") String filePath) {
        this.objectMapper = objectMapper;
        this.filePath = Path.of(filePath);
    }

    public boolean exists() {
        return Files.exists(filePath) && Files.isRegularFile(filePath);
    }

    public ScheduleState read() {
        try {
            return objectMapper.readValue(filePath.toFile(), ScheduleState.class);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to read legacy schedule data", e);
        }
    }

    public String getFilePath() {
        return filePath.toAbsolutePath().toString();
    }
}
