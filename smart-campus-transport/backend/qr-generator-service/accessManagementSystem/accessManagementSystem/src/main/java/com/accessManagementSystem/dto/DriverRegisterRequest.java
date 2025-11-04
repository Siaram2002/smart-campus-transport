package com.accessManagementSystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DriverRegisterRequest {
    private String driverId;
    private String name;
    private String busNumber;
    private String username;
    private String password;
    private String email;
    private String phone;
}
