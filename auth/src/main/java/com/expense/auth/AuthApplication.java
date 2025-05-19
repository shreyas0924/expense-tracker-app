package com.expense.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@EntityScan(basePackages = { "com.expense.auth.entities" })
@EnableJpaRepositories(basePackages = { "com.expense.auth.repository" })
@ComponentScan(basePackages = { "com.expense.auth.auth", "com.expense.auth.controller", "com.expense.auth.service",
		"com.expense.auth.eventProducer" })
public class AuthApplication {
	public static void main(String[] args) {
		SpringApplication.run(AuthApplication.class, args);
	}

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**") // Allow all endpoints
						.allowedOrigins("http://localhost:5173") // Restrict to frontend
						.allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS") // Allow all methods
						.allowedHeaders("*") // Allow all headers
						.allowCredentials(true); // Allow credentials (cookies, authorization headers)
			}
		};
	}

}
