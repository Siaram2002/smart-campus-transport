package com.accessManagementSystem.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		// ✅ Tell Spring Boot to serve /qr_codes/** from the project folder
		String projectPath = System.getProperty("user.dir") + "/qr_codes/";
		registry.addResourceHandler("/qr_codes/**").addResourceLocations("file:" + projectPath);
	}
}
