package com.accessManagementSystem.service;

import com.accessManagementSystem.dto.DriverRegisterRequest;
import com.accessManagementSystem.dto.LoginRequest;
import com.accessManagementSystem.dto.LoginResponse;
import com.accessManagementSystem.entities.Driver;
import com.accessManagementSystem.repository.DriverRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class DriverService {

    @Autowired
    private DriverRepository driverRepository;

    public Driver registerDriver(DriverRegisterRequest request) {
        try {
            log.info("🧾 Registering driver: {}", request.getDriverId());

            Driver driver = new Driver();
            driver.setDriverId(request.getDriverId());
            driver.setName(request.getName());
            driver.setBusNumber(request.getBusNumber());
            driver.setUsername(request.getUsername());
            driver.setPassword(request.getPassword());
            driver.setEmail(request.getEmail());
            driver.setPhone(request.getPhone());

            return driverRepository.save(driver);
        } catch (Exception e) {
            log.error("❌ Error registering driver: {}", e.getMessage());
            throw new RuntimeException("Failed to register driver");
        }
    }

    public LoginResponse login(LoginRequest request) {
        log.info("🔑 Driver login attempt: {}", request.getUsername());
        Optional<Driver> driverOpt = driverRepository.findByUsername(request.getUsername());

        if (driverOpt.isPresent()) {
            Driver driver = driverOpt.get();
            if (driver.getPassword().equals(request.getPassword())) {
                log.info("✅ Driver login successful: {}", driver.getUsername());
                return new LoginResponse(
                        "SUCCESS",
                        "Login successful",
                        driver.getDriverId(),
                        driver.getName(),
                        "DRIVER",
                        driver.getEmail(),
                        driver.getPhone(),
                        null // 🚫 No QR for drivers
                );
            }
        }
        log.warn("❌ Invalid driver login for: {}", request.getUsername());
        return new LoginResponse("FAILED", "Invalid username or password", null, null,
                "DRIVER", null, null, null);
    }
}
