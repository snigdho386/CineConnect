package com.cineconnect.movieengine;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class MovieEngine {

	public static void main(String[] args) {
		SpringApplication.run(MovieEngine.class, args);
	}
}