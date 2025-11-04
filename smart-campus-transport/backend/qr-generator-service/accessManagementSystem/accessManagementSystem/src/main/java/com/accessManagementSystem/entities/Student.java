package com.accessManagementSystem.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "students")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String studentId; // e.g., STU101 — used in QR code

    @Column(nullable = false)
    private String name;

    private String department;
    private String busId;
    private String busStop;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    private LocalDate validityFrom;
    private LocalDate validityTo;

    @Enumerated(EnumType.STRING)
    private StudentStatus studentStatus;

    private String qrImageUrl;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    private String phoneNumber;
}
