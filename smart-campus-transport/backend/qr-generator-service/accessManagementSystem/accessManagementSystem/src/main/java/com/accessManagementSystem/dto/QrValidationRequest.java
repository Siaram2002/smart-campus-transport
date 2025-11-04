package com.accessManagementSystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO used when a driver scans a student's QR code.
 * The mobile app sends this data to the backend for validation.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QrValidationRequest {

    /**
     * The encrypted QR code content (AES-encrypted student ID)
     */
    private String qrHash;

    /**
     * The driver’s unique ID (e.g., DRV101) who scanned the QR
     */
    private String driverId;

    /**
     * Optional — current bus number or location if you want to log scan context
     */
    private String busNumber;
}
