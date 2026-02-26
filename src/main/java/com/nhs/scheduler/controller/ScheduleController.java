package com.nhs.scheduler.controller;

import com.nhs.scheduler.model.AssignmentOverrideRequest;
import com.nhs.scheduler.model.ScheduleResult;
import com.nhs.scheduler.model.ScheduleState;
import com.nhs.scheduler.service.ScheduleService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ScheduleController {
    private final ScheduleService scheduleService;

    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @GetMapping("/state")
    public ScheduleState getState() {
        return scheduleService.getState();
    }

    @PutMapping("/state")
    public ScheduleState saveState(@Valid @RequestBody ScheduleState state) {
        return scheduleService.replaceState(state);
    }

    @GetMapping("/schedule")
    public ScheduleResult getSchedule() {
        return scheduleService.getSchedule();
    }

    @PostMapping("/schedule")
    public ScheduleResult generateSchedule() {
        return scheduleService.generateSchedule();
    }

    @PutMapping("/schedule/override")
    public ScheduleResult overrideScheduleAssignment(@Valid @RequestBody AssignmentOverrideRequest request) {
        return scheduleService.overrideAssignedEmployee(request.getSessionId(), request.getEmployeeId());
    }

    @GetMapping("/meta")
    public ResponseEntity<Map<String, String>> meta() {
        Map<String, String> response = new HashMap<>();
        response.put("dataFile", scheduleService.getDataFilePath());
        return ResponseEntity.ok(response);
    }
}
