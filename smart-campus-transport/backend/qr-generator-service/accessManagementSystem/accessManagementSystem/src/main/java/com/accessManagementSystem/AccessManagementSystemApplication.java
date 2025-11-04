package com.accessManagementSystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class AccessManagementSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(AccessManagementSystemApplication.class, args);
	}

}
