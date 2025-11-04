package com.accessManagementSystem.utilities;

import lombok.extern.slf4j.Slf4j;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

@Slf4j
public class FileUtil {

    public static String encodeFileToBase64(String filePath) {
        try {
            byte[] fileBytes = Files.readAllBytes(Paths.get(filePath));
            log.debug("📄 File read successfully from: {}", filePath);
            return Base64.getEncoder().encodeToString(fileBytes);
        } catch (IOException e) {
            log.error("❌ Failed to read file: {} | Reason: {}", filePath, e.getMessage());
            throw new RuntimeException("Error converting file to Base64: " + e.getMessage(), e);
        }
    }
}
