package com.javsolutions.jw.lifeandministry;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * Main application class that starts the Spring Boot application and launches the JavaFX UI.
 */
@SpringBootApplication
public class LifeAndMinistryApplication {

    private static ConfigurableApplicationContext springContext;

    /**
     * Main method that starts the application.
     * 
     * @param args command line arguments
     */
    public static void main(String[] args) {
        // Start the Spring Boot application
        springContext = SpringApplication.run(LifeAndMinistryApplication.class, args);

        // Launch the JavaFX application
        Application.launch(JavaFxApplication.class, args);
    }

    /**
     * JavaFX application class that is launched by the main method.
     */
    public static class JavaFxApplication extends Application {

        @Override
        public void init() {
            // No need to initialize MongoDB connection here as Spring Boot will handle it
        }

        @Override
        public void start(Stage stage) throws Exception {
            // Set the Spring context for the FXMLLoaderFactory
            FXMLLoaderFactory.setSpringContext(springContext);
            // Load and show the application view
            FXMLLoaderFactory.loadFxmlAndShow("application-view.fxml", "Life and Ministry", stage);
        }

        @Override
        public void stop() {
            // Close the Spring context when the JavaFX application stops
            springContext.close();
            Platform.exit();
        }
    }
}
