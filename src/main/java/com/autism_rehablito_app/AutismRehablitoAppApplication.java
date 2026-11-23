package com.autism_rehablito_app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class AutismRehablitoAppApplication {
	public static void main(String[] args) {
		SpringApplication.run(AutismRehablitoAppApplication.class, args);
	}

}
