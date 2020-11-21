package com.rest.webservices;

import java.util.Collections;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class OcrRestApplication {

	public static void main(String[] args) {
		//SpringApplication.run(OcrRestApplication.class, args);
		
		SpringApplication app = new SpringApplication(OcrRestApplication.class);
        app.setDefaultProperties(Collections
          .singletonMap("server.port", "8080"));
        app.run(args);
	}

}

