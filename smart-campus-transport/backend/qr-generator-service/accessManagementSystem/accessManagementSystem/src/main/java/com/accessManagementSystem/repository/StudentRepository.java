package com.accessManagementSystem.repository;

import com.accessManagementSystem.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

	/**
	 * Find a student by their unique student ID (used in QR validation).
	 */
	Optional<Student> findByStudentId(String studentId);

	/**
	 * Find a student by email (used in login).
	 */
	Optional<Student> findByEmail(String email);

	/**
	 * Find a student by phone number (optional for OTP login).
	 */
	Optional<Student> findByPhoneNumber(String phoneNumber);

	Optional<Student> findByUsername(String username);

}
