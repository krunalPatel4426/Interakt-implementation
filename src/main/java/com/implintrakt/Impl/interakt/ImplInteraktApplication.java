package com.implintrakt.Impl.interakt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ImplInteraktApplication {

	public static void main(String[] args) {
		SpringApplication.run(ImplInteraktApplication.class, args);
	}

}
