package com.example.polynomial;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;

@SpringBootApplication(exclude = UserDetailsServiceAutoConfiguration.class)
public class WebPolynomialApp {

	public static void main(String[] args) {
		SpringApplication.run(WebPolynomialApp.class, args);
	}
}
