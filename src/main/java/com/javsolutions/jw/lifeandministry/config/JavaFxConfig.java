package com.javsolutions.jw.lifeandministry.config;

import com.javsolutions.jw.lifeandministry.HelloController;
import com.javsolutions.jw.lifeandministry.repository.StudentRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * JavaFX configuration class.
 * This class configures JavaFX controllers as Spring beans.
 */
@Configuration
public class JavaFxConfig {

    /**
     * Create a HelloController bean.
     * This is needed because the FXMLLoader will look for a bean of this type.
     * 
     * @param studentRepository the student repository
     * @return a HelloController instance
     */
    @Bean
    public HelloController helloController(StudentRepository studentRepository) {
        // Create a HelloController with the injected StudentRepository
        return new HelloController(studentRepository);
    }
}
