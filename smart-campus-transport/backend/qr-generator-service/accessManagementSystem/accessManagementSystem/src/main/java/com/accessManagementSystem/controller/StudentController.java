package com.accessManagementSystem.controller;

import com.accessManagementSystem.dto.LoginRequest;
import com.accessManagementSystem.dto.LoginResponse;
import com.accessManagementSystem.dto.StudentRegisterRequest;
import com.accessManagementSystem.entities.Student;
import com.accessManagementSystem.service.StudentService;
import com.accessManagementSystem.utilities.ResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/student")
@CrossOrigin(origins = "*")
public class StudentController {

    @Autowired
    private StudentService studentService;

    /**
     * 🎓 Register a new student
     */
    @PostMapping("/register")
    public ResponseEntity<?> registerStudent(@RequestBody StudentRegisterRequest request) {
        try {
            log.info("🎓 Student registration request received: {}", request.getStudentId());

            // ✅ Input validation
            if (request.getStudentId() == null || request.getStudentId().isEmpty() ||
                request.getName() == null || request.getName().isEmpty() ||
                request.getDepartment() == null || request.getDepartment().isEmpty() ||
                request.getEmail() == null || request.getEmail().isEmpty() ||
                request.getPhoneNumber() == null || request.getPhoneNumber().isEmpty()) {
                return ResponseUtil.badRequest("Missing required student registration fields");
            }

            Student student = studentService.registerStudent(request);
            log.info("✅ Student registered successfully: {}", student.getStudentId());
            return ResponseUtil.success(student, "Student registered successfully");

        } catch (Exception e) {
            log.error("❌ Error registering student: ", e);
            return ResponseUtil.error("Failed to register student: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 🔑 Student login endpoint
     */
    @PostMapping("/login")
    public ResponseEntity<?> studentLogin(@RequestBody LoginRequest request) {
        try {
            log.info("🔑 Student login attempt: {}", request.getUsername());

            // ✅ Basic validation
            if (request.getUsername() == null || request.getUsername().isEmpty() ||
                request.getPassword() == null || request.getPassword().isEmpty()) {
                return ResponseUtil.badRequest("Username or password missing");
            }

            LoginResponse response = studentService.login(request);

            if ("SUCCESS".equalsIgnoreCase(response.getStatus())) {
                log.info("✅ Login successful for student: {}", request.getUsername());
                return ResponseUtil.success(response, "Login successful");
            } else {
                log.warn("⚠️ Invalid login attempt for student: {}", request.getUsername());
                return ResponseUtil.error("Invalid username or password", HttpStatus.UNAUTHORIZED);
            }

        } catch (Exception e) {
            log.error("❌ Student login failed: ", e);
            return ResponseUtil.error("Login failed: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
