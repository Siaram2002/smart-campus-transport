package com.accessManagementSystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {

    private String status;    // SUCCESS / FAILED
    private String message;
    private String userId;    // studentId or driverId
    private String name;
    private String role;      // STUDENT / DRIVER
    private String email;
    private String phone;
    private String qrImageUrl; // added for students only
}
