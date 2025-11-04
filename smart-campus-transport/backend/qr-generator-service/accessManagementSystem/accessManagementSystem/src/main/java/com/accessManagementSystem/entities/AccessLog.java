package com.accessManagementSystem.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "access_logs")
@Data
public class AccessLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String studentId;
    private String driverId;
    private boolean accessGranted;
    private String remarks;
    private LocalDateTime scannedAt = LocalDateTime.now();
}
