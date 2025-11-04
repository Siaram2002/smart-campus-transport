package com.accessManagementSystem.service;

import com.accessManagementSystem.dto.LoginRequest;
import com.accessManagementSystem.dto.LoginResponse;
import com.accessManagementSystem.dto.StudentRegisterRequest;
import com.accessManagementSystem.entities.PaymentStatus;
import com.accessManagementSystem.entities.Student;
import com.accessManagementSystem.entities.StudentStatus;
import com.accessManagementSystem.repository.StudentRepository;
import com.accessManagementSystem.utilities.QRCodeGeneratorUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class StudentService {

	@Autowired
	private StudentRepository studentRepository;

	@Autowired
	private QRCodeGeneratorUtil qrCodeGeneratorUtil;

	/**
	 * Registers a new student and auto-generates a QR code.
	 */
	public Student registerStudent(StudentRegisterRequest request) {
		try {
			log.info("🧾 Registering student: {}", request.getStudentId());

			Student student = new Student();
			student.setStudentId(request.getStudentId());
			student.setName(request.getName());
			student.setDepartment(request.getDepartment());
			student.setBusId(request.getBusId());
			student.setBusStop(request.getBusStop());
			student.setUsername(request.getUsername());
			student.setPassword(request.getPassword());
			student.setEmail(request.getEmail());
			student.setPhoneNumber(request.getPhoneNumber());
			student.setValidityFrom(request.getValidityFrom());
			student.setValidityTo(request.getValidityTo());
			student.setPaymentStatus(PaymentStatus.PAID);
			student.setStudentStatus(StudentStatus.VALID);

			// ✅ Generate QR and store URL
			String qrUrl = qrCodeGeneratorUtil.generateEncryptedQRCode(request.getStudentId());
			student.setQrImageUrl(qrUrl);

			return studentRepository.save(student);
		} catch (Exception e) {
			log.error("❌ Error registering student: ", e);
			throw new RuntimeException("Failed to register student: " + e.getMessage(), e);
		}
	}

	/**
	 * Handles student login.
	 */
	public LoginResponse login(LoginRequest request) {
		log.info("🔑 Student login attempt: {}", request.getUsername());
		Optional<Student> studentOpt = studentRepository.findByUsername(request.getUsername());

		if (studentOpt.isPresent()) {
			Student student = studentOpt.get();
			if (student.getPassword().equals(request.getPassword())) {
				log.info("✅ Student login successful: {}", student.getUsername());
				return new LoginResponse("SUCCESS", "Login successful", student.getStudentId(), student.getName(),
						"STUDENT", student.getEmail(), student.getPhoneNumber(), student.getQrImageUrl() // ✅ full URL
																											// from util
				);
			}
		}
		log.warn("❌ Invalid login for: {}", request.getUsername());
		return new LoginResponse("FAILED", "Invalid username or password", null, null, "STUDENT", null, null, null);
	}
}
