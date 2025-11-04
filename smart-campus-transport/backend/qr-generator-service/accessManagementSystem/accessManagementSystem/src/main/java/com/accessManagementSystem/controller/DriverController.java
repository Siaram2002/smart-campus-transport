package com.accessManagementSystem.controller;

import com.accessManagementSystem.dto.DriverRegisterRequest;
import com.accessManagementSystem.dto.LoginRequest;
import com.accessManagementSystem.dto.LoginResponse;
import com.accessManagementSystem.entities.Driver;
import com.accessManagementSystem.service.DriverService;
import com.accessManagementSystem.utilities.ResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/driver")
@CrossOrigin(origins = "*")
public class DriverController {

    @Autowired
    private DriverService driverService;

    /**
     * 🚐 Register a new driver.
     */
    @PostMapping("/register")
    public ResponseEntity<?> registerDriver(@RequestBody DriverRegisterRequest request) {
        try {
            log.info("🚌 Register API called for driverId: {}", request.getDriverId());

            // Basic validation
            if (request.getDriverId() == null || request.getName() == null ||
                request.getEmail() == null || request.getPhone() == null) {
                return ResponseUtil.badRequest("Missing required fields for driver registration");
            }

            Driver driver = driverService.registerDriver(request);
            return ResponseUtil.success(driver, "Driver registered successfully");

        } catch (Exception e) {
            log.error("❌ Error registering driver: ", e);
            return ResponseUtil.error("Failed to register driver: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 🔑 Driver login endpoint.
     */
    @PostMapping("/login")
    public ResponseEntity<?> driverLogin(@RequestBody LoginRequest request) {
        try {
            log.info("🔑 Driver login attempt for username: {}", request.getUsername());

            if (request.getUsername() == null || request.getPassword() == null) {
                return ResponseUtil.badRequest("Username or password missing");
            }

            LoginResponse response = driverService.login(request);

            if ("SUCCESS".equalsIgnoreCase(response.getStatus())) {
                log.info("✅ Driver login successful for {}", request.getUsername());
                return ResponseUtil.success(response, "Login successful");
            } else {
                log.warn("⚠️ Invalid credentials for driver: {}", request.getUsername());
                return ResponseUtil.error("Invalid username or password", HttpStatus.UNAUTHORIZED);
            }

        } catch (Exception e) {
            log.error("❌ Driver login failed: ", e);
            return ResponseUtil.error("Login failed: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
