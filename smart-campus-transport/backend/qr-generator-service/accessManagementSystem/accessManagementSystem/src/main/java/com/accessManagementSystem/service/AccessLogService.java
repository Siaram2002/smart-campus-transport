package com.accessManagementSystem.service;

import com.accessManagementSystem.entities.AccessLog;
import com.accessManagementSystem.repository.AccessLogRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class AccessLogService {

	@Autowired
	private AccessLogRepository accessLogRepository;

	/**
	 * Logs access asynchronously to improve performance under high load.
	 */
	@Async("taskExecutor")
	public void logAccessAsync(String studentId, String driverId, boolean accessGranted, String remarks) {
		try {
			AccessLog logEntry = new AccessLog();
			logEntry.setStudentId(studentId);
			logEntry.setDriverId(driverId);
			logEntry.setAccessGranted(accessGranted);
			logEntry.setRemarks(remarks);

			accessLogRepository.save(logEntry);
			log.info("📜 Log saved async: student={}, driver={}, granted={}", studentId, driverId, accessGranted);
		} catch (Exception e) {
			log.error("❌ Failed async log: {}", e.getMessage());
		}
	}

	public List<AccessLog> getLogsByStudent(String studentId) {
		log.info("🔍 Fetching logs for student: {}", studentId);
		return accessLogRepository.findByStudentId(studentId);
	}

	public List<AccessLog> getLogsByDriver(String driverId) {
		log.info("🔍 Fetching logs for driver: {}", driverId);
		return accessLogRepository.findByDriverId(driverId);
	}
}
