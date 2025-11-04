package com.accessManagementSystem.controller;

import com.accessManagementSystem.dto.QrValidationRequest;
import com.accessManagementSystem.dto.QrValidationResponse;
import com.accessManagementSystem.service.QRValidationService;
import com.accessManagementSystem.utilities.ResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@Slf4j
@RestController
@RequestMapping("/api/qr")
@CrossOrigin(origins = "*")
public class QRValidationController {

    @Autowired
    private QRValidationService qrValidationService;

    /**
     * 📲 Validate QR Code — called when a driver scans a student's QR.
     */
    @PostMapping("/validate")
    public ResponseEntity<?> validateQr(@RequestBody QrValidationRequest request) {
        try {
            log.info("📲 QR validation request received from driver: {} | QR hash: {}", 
                     request.getDriverId(), request.getQrHash());

            // 🧩 Basic request validation
            if (request.getQrHash() == null || request.getQrHash().isEmpty()) {
                return ResponseUtil.badRequest("QR hash is missing or invalid");
            }
            if (request.getDriverId() == null || request.getDriverId().isEmpty()) {
                return ResponseUtil.badRequest("Driver ID is missing");
            }

            // 🚀 Call async QR validation service
            CompletableFuture<QrValidationResponse> futureResponse = qrValidationService.validateQr(request);

            // ✅ Block until result is ready (few milliseconds typically)
            QrValidationResponse response = futureResponse.join();

            if ("VALID".equalsIgnoreCase(response.getStatus())) {
                log.info("✅ Access granted for student ID: {}", response.getStudentId());
                return ResponseUtil.success(response, "Access granted");
            } else if ("ERROR".equalsIgnoreCase(response.getStatus())) {
                log.warn("⚠️ Invalid or corrupted QR scanned by driver: {}", request.getDriverId());
                return ResponseUtil.error("Invalid or corrupted QR data", HttpStatus.BAD_REQUEST);
            } else {
                log.warn("🚫 Access denied for student: {}", response.getStudentId());
                return ResponseUtil.error(response.getMessage(), HttpStatus.UNAUTHORIZED);
            }

        } catch (Exception e) {
            log.error("❌ Error during QR validation: ", e);
            return ResponseUtil.error("Internal server error during QR validation", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
