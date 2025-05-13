package com.javsolutions.jw.lifeandministry;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.context.ApplicationContext;

import java.io.IOException;
import java.net.URL;

/**
 * Factory class for creating FXMLLoader instances that use Spring to instantiate controllers.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FXMLLoaderFactory {

    private static ApplicationContext springContext;

    /**
     * Set the Spring application context.
     * 
     * @param context the Spring application context
     * @throws IllegalArgumentException if context is null
     */
    public static void setSpringContext(ApplicationContext context) {
        if (context == null) {
            throw new IllegalArgumentException("Spring context cannot be null");
        }
        springContext = context;
    }

    /**
     * Create an FXMLLoader that uses Spring to instantiate controllers.
     * 
     * @param fxmlPath the path to the FXML file
     * @return an FXMLLoader instance
     * @throws IllegalArgumentException if the FXML resource cannot be found
     */
    public static FXMLLoader createLoader(String fxmlPath) {
        if (fxmlPath == null) {
            throw new IllegalArgumentException("FXML path cannot be null");
        }

        URL fxmlUrl = FXMLLoaderFactory.class.getResource(fxmlPath);
        if (fxmlUrl == null) {
            throw new IllegalArgumentException("FXML resource not found: " + fxmlPath);
        }

        FXMLLoader loader = new FXMLLoader(fxmlUrl);

        // Set a controller factory that uses Spring to instantiate controllers
        if (springContext == null) {
            throw new IllegalStateException("Spring context has not been set");
        }
        loader.setControllerFactory(springContext::getBean);

        return loader;
    }

    /**
     * Load an FXML file and show it in a stage.
     * 
     * @param fxmlPath the path to the FXML file
     * @param title the title of the stage
     * @param stage the stage to show the FXML in
     * @throws IOException if the FXML file cannot be loaded
     */
    public static void loadFxmlAndShow(String fxmlPath, String title, Stage stage) throws IOException {
        FXMLLoader loader = createLoader(fxmlPath);
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setTitle(title);
        stage.setScene(scene);
        stage.show();
    }
}
