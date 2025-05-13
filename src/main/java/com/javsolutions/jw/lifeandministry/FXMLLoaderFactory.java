package com.javsolutions.jw.lifeandministry;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.context.ApplicationContext;

import java.io.IOException;
import java.net.URL;

/**
 * Factory class for creating FXMLLoader instances that use Spring to instantiate controllers.
 */
public class FXMLLoaderFactory {
    
    private static ApplicationContext springContext;
    
    /**
     * Set the Spring application context.
     * 
     * @param context the Spring application context
     */
    public static void setSpringContext(ApplicationContext context) {
        springContext = context;
    }
    
    /**
     * Create an FXMLLoader that uses Spring to instantiate controllers.
     * 
     * @param fxmlPath the path to the FXML file
     * @return an FXMLLoader instance
     */
    public static FXMLLoader createLoader(String fxmlPath) {
        URL fxmlUrl = FXMLLoaderFactory.class.getResource(fxmlPath);
        FXMLLoader loader = new FXMLLoader(fxmlUrl);
        
        // Set a controller factory that uses Spring to instantiate controllers
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