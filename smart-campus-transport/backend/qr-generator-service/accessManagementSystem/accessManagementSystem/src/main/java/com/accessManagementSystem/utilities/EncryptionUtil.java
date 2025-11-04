package com.accessManagementSystem.utilities;

import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Slf4j
public class EncryptionUtil {

	// Must be exactly 16 characters (128-bit)
	private static final String SECRET_KEY = "AMSSecretKey5432"; // ✅ 16 chars

	public static String encrypt(String data) {
		try {
			SecretKeySpec key = new SecretKeySpec(SECRET_KEY.getBytes(), "AES");
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.ENCRYPT_MODE, key);
			byte[] encrypted = cipher.doFinal(data.getBytes());
			System.out.println(Base64.getUrlEncoder().encodeToString(encrypted));
			return Base64.getUrlEncoder().encodeToString(encrypted);
		} catch (Exception e) {
			log.error("❌ Error while encrypting: {}", e.getMessage());
			throw new RuntimeException(e);
		}
	}

	public static String decrypt(String encryptedData) {
		try {
			SecretKeySpec key = new SecretKeySpec(SECRET_KEY.getBytes(), "AES");
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.DECRYPT_MODE, key);
			byte[] decoded = Base64.getUrlDecoder().decode(encryptedData);
			byte[] decrypted = cipher.doFinal(decoded);
			System.out.println(decrypted);
			return new String(decrypted);
		} catch (Exception e) {
			log.error("❌ Error while decrypting: {}", e.getMessage());
			throw new RuntimeException(e);
		}
	}
}
