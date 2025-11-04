package com.accessManagementSystem.controller;

import com.accessManagementSystem.entities.AccessLog;
import com.accessManagementSystem.service.AccessLogService;
import com.accessManagementSystem.utilities.ResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/logs")
@CrossOrigin(origins = "*")
public class AccessLogController {

    @Autowired
    private AccessLogService accessLogService;

    @GetMapping("/student/{studentId}")
    public ResponseEntity<?> getStudentLogs(@PathVariable String studentId) {
        log.info("📜 Fetching access logs for student: {}", studentId);
        List<AccessLog> logs = accessLogService.getLogsByStudent(studentId);
        return ResponseUtil.success(logs, "Logs fetched successfully");
    }

    @GetMapping("/driver/{driverId}")
    public ResponseEntity<?> getDriverLogs(@PathVariable String driverId) {
        log.info("📜 Fetching access logs for driver: {}", driverId);
        List<AccessLog> logs = accessLogService.getLogsByDriver(driverId);
        return ResponseUtil.success(logs, "Logs fetched successfully");
    }
}
