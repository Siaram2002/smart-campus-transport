package com.accessManagementSystem.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;

@Slf4j
@RestController
@RequestMapping("/api/qr/image")
@CrossOrigin(origins = "*")
public class QrImageController {

	private static final String QR_DIR = "qr_codes/";

	/**
	 * Streams QR image safely with correct MIME type.
	 */
	@GetMapping("/{fileName:.+}")
	public ResponseEntity<Resource> getQrImage(@PathVariable String fileName) {
		try {
			File file = new File(QR_DIR + fileName);
			if (!file.exists()) {
				log.warn("⚠️ QR image not found: {}", fileName);
				return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
			}

			Resource resource = new FileSystemResource(file);
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.IMAGE_PNG);
			headers.setCacheControl("no-cache, no-store, must-revalidate");
			
			log.info("✅ Served QR image: {} ({} bytes)", fileName, file.length());

			return new ResponseEntity<>(resource, headers, HttpStatus.OK);

		} catch (Exception e) {
			log.error("❌ Failed to serve QR image {}: {}", fileName, e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
}
