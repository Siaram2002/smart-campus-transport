package com.accessManagementSystem.utilities;

import com.accessManagementSystem.dto.QrValidationResponse;
import com.accessManagementSystem.entities.PaymentStatus;
import com.accessManagementSystem.entities.Student;
import com.accessManagementSystem.entities.StudentStatus;
import com.accessManagementSystem.repository.StudentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

/**
 * Service utility for verifying student QR codes scanned by drivers. Works with
 * static QR codes (AES-encrypted student IDs).
 */
@Service
@Slf4j
public class QRVerificationUtil {

	@Autowired
	private StudentRepository studentRepository;

	/**
	 * Validates the scanned encrypted QR data. Returns a QrValidationResponse with
	 * student info and access result.
	 */
	public QrValidationResponse verifyStudentFromQr(String encryptedQrData) {
		try {
			
			
			
			// Step 1️⃣: Decrypt QR data
			String studentId = EncryptionUtil.decrypt(encryptedQrData);
			log.info("🔍 Decrypted QR data. Student ID extracted: {}", studentId);

			// Step 2️⃣: Find student in DB
			Optional<Student> studentOpt = studentRepository.findByStudentId(studentId);
			if (studentOpt.isEmpty()) {
				log.warn("❌ Student not found for scanned QR: {}", studentId);
				return buildResponse("INVALID", "Student not found.", null, false, "No record found.");
			}

			Student student = studentOpt.get();
			log.debug("✅ Student record found: {}", student.getName());

			// Step 3️⃣: Validation checks
			if (student.getPaymentStatus() != PaymentStatus.PAID) {
				log.warn("❌ Payment not completed for student: {}", student.getStudentId());
				return buildResponse("INVALID", "Payment pending. Access denied.", student, false,
						"Payment not completed.");
			}

			if (student.getStudentStatus() != StudentStatus.VALID) {
				log.warn("🚫 Student is inactive or blocked: {}", student.getStudentId());
				return buildResponse("INVALID", "Student inactive or blocked.", student, false, "Account not active.");
			}

			if (DateUtil.isExpired(student.getValidityTo())) {
				log.warn("📅 Bus pass expired for student: {}", student.getStudentId());
				return buildResponse("INVALID", "Bus pass expired.", student, false, "Validity expired.");
			}

			// Step 4️⃣: If all validations pass
			log.info("✅ Access granted for student: {}", student.getStudentId());
			return buildResponse("VALID", "Access granted.", student, true, "Student verified successfully.");

		} catch (Exception e) {
			log.error("❌ QR Verification failed. Reason: {}", e.getMessage());
			return buildResponse("INVALID", "Invalid or corrupted QR code.", null, false, "Decryption failed.");
		}
	}

	/**
	 * Helper to build a standardized QrValidationResponse object.
	 */
	private QrValidationResponse buildResponse(String status, String message, Student student, boolean accessGranted,
			String remarks) {
		if (student == null) {
			return new QrValidationResponse(status, message, null, null, null, null, null, null, null, null,
					accessGranted, remarks);
		}

		return new QrValidationResponse(status, message, student.getStudentId(), student.getName(),
				student.getDepartment(), student.getBusId(), student.getPaymentStatus(), student.getValidityFrom(),
				student.getValidityTo(), student.getBusStop(), accessGranted, remarks);
	}
}
