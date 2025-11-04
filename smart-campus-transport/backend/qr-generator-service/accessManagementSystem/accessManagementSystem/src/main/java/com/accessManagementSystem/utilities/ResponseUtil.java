package com.accessManagementSystem.utilities;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

/**
 * Utility class for consistent API responses.
 */
public class ResponseUtil {

    public static ResponseEntity<?> success(Object data, String message) {
        Map<String, Object> body = new HashMap<>();
        body.put("status", "SUCCESS");
        body.put("message", message);
        body.put("data", data);
        return new ResponseEntity<>(body, HttpStatus.OK);
    }

    public static ResponseEntity<?> created(Object data, String message) {
        Map<String, Object> body = new HashMap<>();
        body.put("status", "CREATED");
        body.put("message", message);
        body.put("data", data);
        return new ResponseEntity<>(body, HttpStatus.CREATED);
    }

    public static ResponseEntity<?> error(String message, HttpStatus status) {
        Map<String, Object> body = new HashMap<>();
        body.put("status", "ERROR");
        body.put("message", message);
        return new ResponseEntity<>(body, status);
    }

    public static ResponseEntity<?> notFound(String message) {
        return error(message, HttpStatus.NOT_FOUND);
    }

    public static ResponseEntity<?> badRequest(String message) {
        return error(message, HttpStatus.BAD_REQUEST);
    }
}
