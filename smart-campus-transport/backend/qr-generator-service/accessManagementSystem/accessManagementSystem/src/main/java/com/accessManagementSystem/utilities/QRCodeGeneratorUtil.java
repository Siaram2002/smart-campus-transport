package com.accessManagementSystem.utilities;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;

@Slf4j
@Component
public class QRCodeGeneratorUtil {

    private static final String QR_OUTPUT_DIR = "qr_codes/";

    @Value("${app.base-url:http://localhost}")
    private String baseUrl;

    @Value("${server.port:8080}")
    private int serverPort;

    @Value("${qr.size:300}")
    private int qrSize;

    /**
     * ✅ Generates encrypted QR code for a student and returns the public access URL.
     */
    public String generateEncryptedQRCode(String studentId) {
        try {
            // 🔐 Encrypt student ID
            String encryptedData = EncryptionUtil.encrypt(studentId);

            // 📂 Ensure directory exists
            File directory = new File(QR_OUTPUT_DIR);
            if (!directory.exists()) directory.mkdirs();

            String fileName = studentId + ".png";
            String filePath = QR_OUTPUT_DIR + fileName;
            Path path = FileSystems.getDefault().getPath(filePath);

            // 🧹 Optional: Delete old file to avoid cache conflicts
            File file = new File(filePath);
            if (file.exists()) {
                boolean deleted = file.delete();
                if (deleted) log.info("🧹 Old QR deleted for {}", studentId);
            }

            // 🧾 Generate QR code
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(encryptedData, BarcodeFormat.QR_CODE, qrSize, qrSize);
            MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);

            log.info("✅ QR code generated for student {} at {}", studentId, filePath);

            // 🌍 Return accessible public URL
            String qrUrl = String.format("%s:%d/api/qr/image/%s", baseUrl, serverPort, fileName);
            log.info("🌐 Public QR URL: {}", qrUrl);
            return qrUrl;

        } catch (WriterException | IOException e) {
            log.error("❌ Error generating QR code for {}: {}", studentId, e.getMessage(), e);
            throw new RuntimeException("Error generating QR code: " + e.getMessage(), e);
        }
    }
}
