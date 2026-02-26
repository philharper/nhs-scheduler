package com.nhs.scheduler.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhs.scheduler.model.ScheduleState;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Service
public class FileStateStore {
    private final ObjectMapper objectMapper;
    private final Path filePath;
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    public FileStateStore(ObjectMapper objectMapper,
                          @Value("${scheduler.data-file:data/schedule-state.json}") String filePath) {
        this.objectMapper = objectMapper;
        this.filePath = Path.of(filePath);
    }

    @PostConstruct
    public void init() {
        lock.writeLock().lock();
        try {
            Path parent = filePath.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }
            if (Files.notExists(filePath)) {
                objectMapper.writerWithDefaultPrettyPrinter().writeValue(filePath.toFile(), new ScheduleState());
            }
        } catch (IOException e) {
            throw new IllegalStateException("Failed to initialize data file: " + filePath, e);
        } finally {
            lock.writeLock().unlock();
        }
    }

    public ScheduleState read() {
        lock.readLock().lock();
        try {
            return objectMapper.readValue(filePath.toFile(), ScheduleState.class);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to read schedule data", e);
        } finally {
            lock.readLock().unlock();
        }
    }

    public void write(ScheduleState state) {
        lock.writeLock().lock();
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(filePath.toFile(), state);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to write schedule data", e);
        } finally {
            lock.writeLock().unlock();
        }
    }

    public String getFilePath() {
        return filePath.toAbsolutePath().toString();
    }
}
