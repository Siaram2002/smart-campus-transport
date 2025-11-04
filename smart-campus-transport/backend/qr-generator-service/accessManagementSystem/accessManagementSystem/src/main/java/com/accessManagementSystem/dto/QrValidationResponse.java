package com.accessManagementSystem.dto;

import com.accessManagementSystem.entities.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Response DTO sent back to the driver app after QR verification.
 * It contains student info, status, and remarks.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QrValidationResponse {

    /**
     * VALID / INVALID - final status of the QR verification.
     */
    private String status;

    /**
     * Message or reason for result (e.g., "Access granted", "Payment pending").
     */
    private String message;

    /**
     * Student's unique ID (roll/registration ID).
     */
    private String studentId;

    /**
     * Student's full name.
     */
    private String studentName;

    /**
     * Department name (e.g., "CSE", "EEE").
     */
    private String department;

    /**
     * Assigned bus ID or number.
     */
    private String busId;

    /**
     * Student's payment status (PAID, UNPAID, PENDING).
     */
    private PaymentStatus paymentStatus;

    /**
     * Bus pass validity start date.
     */
    private LocalDate validityFrom;

    /**
     * Bus pass validity end date.
     */
    private LocalDate validityTo;

    /**
     * Student’s registered bus stop name.
     */
    private String busStop;

    /**
     * Whether access is granted (true) or denied (false).
     */
    private boolean accessGranted;

    /**
     * Optional additional note or remark.
     */
    private String remarks;
}
