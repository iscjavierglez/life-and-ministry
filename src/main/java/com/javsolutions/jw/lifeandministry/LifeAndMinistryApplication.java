package com.javsolutions.jw.lifeandministry;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main application class that starts the Spring Boot application with Thymeleaf UI.
 * This is a standard Spring Boot web application that uses Thymeleaf templates
 */
@SpringBootApplication
public class LifeAndMinistryApplication {

    /**
     * Main method that starts the application.
     * 
     * @param args command line arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(LifeAndMinistryApplication.class, args);
    }
}
