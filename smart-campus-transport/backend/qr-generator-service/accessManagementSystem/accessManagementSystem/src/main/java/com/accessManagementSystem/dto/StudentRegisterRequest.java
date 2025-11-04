package com.accessManagementSystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentRegisterRequest {
    private String studentId;
    private String name;
    private String department;
    private String busId;
    private String busStop;
    private String username;
    private String password;
    private String email;
    private String phoneNumber;
    private LocalDate validityFrom;
    private LocalDate validityTo;
}
