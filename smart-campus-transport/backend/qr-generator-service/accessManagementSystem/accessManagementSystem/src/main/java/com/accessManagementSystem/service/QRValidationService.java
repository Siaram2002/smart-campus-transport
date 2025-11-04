package com.accessManagementSystem.service;

import com.accessManagementSystem.dto.QrValidationRequest;
import com.accessManagementSystem.dto.QrValidationResponse;
import com.accessManagementSystem.entities.PaymentStatus;
import com.accessManagementSystem.entities.Student;
import com.accessManagementSystem.entities.StudentStatus;
import com.accessManagementSystem.repository.StudentRepository;
import com.accessManagementSystem.utilities.DateUtil;
import com.accessManagementSystem.utilities.EncryptionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class QRValidationService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private AccessLogService accessLogService;

    /**
     * Validate QR asynchronously — scalable for multiple concurrent scans.
     */
    @Async("taskExecutor")
    public CompletableFuture<QrValidationResponse> validateQr(QrValidationRequest request) {
        try {
            log.info("🧾 Validating QR for Driver: {} | Bus: {}", request.getDriverId(), request.getBusNumber());

            // 🔐 Decrypt student ID
            String decryptedStudentId = EncryptionUtil.decrypt(request.getQrHash());
            log.info("🔍 Decrypted Student ID: {}", decryptedStudentId);

            Optional<Student> studentOpt = studentRepository.findByStudentId(decryptedStudentId);
            if (studentOpt.isEmpty()) {
                log.warn("⚠️ Student not found for decrypted ID: {}", decryptedStudentId);
                accessLogService.logAccessAsync(decryptedStudentId, request.getDriverId(), false, "Student not found");
                return completed(invalid(null, "Student not found"));
            }

            Student student = studentOpt.get();

            // 💰 Check payment status
            if (student.getPaymentStatus() != PaymentStatus.PAID) {
                accessLogService.logAccessAsync(student.getStudentId(), request.getDriverId(), false, "Payment pending");
                return completed(invalid(student, "Payment pending"));
            }

            // 🚫 Check student status
            if (student.getStudentStatus() != StudentStatus.VALID) {
                accessLogService.logAccessAsync(student.getStudentId(), request.getDriverId(), false, "Student inactive or blocked");
                return completed(invalid(student, "Student inactive or blocked"));
            }

            // ⏰ Check validity date
            if (DateUtil.isExpired(student.getValidityTo())) {
                accessLogService.logAccessAsync(student.getStudentId(), request.getDriverId(), false, "Bus pass expired");
                return completed(invalid(student, "Bus pass expired"));
            }

            // ✅ All checks passed — Access granted
            accessLogService.logAccessAsync(student.getStudentId(), request.getDriverId(), true, "Access granted");
            log.info("✅ Access granted for student: {}", student.getStudentId());

            return completed(valid(student));

        } catch (Exception e) {
            log.error("❌ QR validation failed: ", e);
            accessLogService.logAccessAsync(null, request.getDriverId(), false, "Invalid or corrupted QR");
            return completed(new QrValidationResponse(
                    "ERROR",
                    "Invalid or corrupted QR data",
                    null, null, null, null, null, null, null, null,
                    false, "Invalid QR"));
        }
    }

    /**
     * ✅ Valid student response builder.
     */
    private QrValidationResponse valid(Student student) {
        return new QrValidationResponse(
                "VALID",
                "Access granted",
                student.getStudentId(),
                student.getName(),
                student.getDepartment(),
                student.getBusId(),
                student.getPaymentStatus(),
                student.getValidityFrom(),
                student.getValidityTo(),
                student.getBusStop(),
                true,
                "Access granted");
    }

    /**
     * ❌ Invalid student response builder.
     */
    private QrValidationResponse invalid(Student student, String reason) {
        if (student == null) {
            return new QrValidationResponse(
                    "INVALID",
                    reason,
                    null, null, null, null, null, null, null, null,
                    false, reason);
        }

        return new QrValidationResponse(
                "INVALID",
                reason,
                student.getStudentId(),
                student.getName(),
                student.getDepartment(),
                student.getBusId(),
                student.getPaymentStatus(),
                student.getValidityFrom(),
                student.getValidityTo(),
                student.getBusStop(),
                false,
                reason);
    }

    private CompletableFuture<QrValidationResponse> completed(QrValidationResponse response) {
        return CompletableFuture.completedFuture(response);
    }
}
