module com.javsolutions.jw.lifeandministry {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;

    // Spring Boot modules
    requires spring.core;
    requires spring.beans;
    requires spring.context;
    requires spring.boot;
    requires spring.boot.autoconfigure;
    requires spring.data.mongodb;
    requires spring.data.commons;

    // MongoDB modules
    requires org.mongodb.driver.sync.client;
    requires transitive org.mongodb.bson;
    requires org.mongodb.driver.core;

    // Lombok
    requires static lombok;

    // Logging
    requires org.slf4j;

    // Open packages to JavaFX and Spring
    opens com.javsolutions.jw.lifeandministry to javafx.fxml, spring.core;
    opens com.javsolutions.jw.lifeandministry.model to spring.core;
    opens com.javsolutions.jw.lifeandministry.repository to spring.core;
    opens com.javsolutions.jw.lifeandministry.config to spring.core;
    opens com.javsolutions.jw.lifeandministry.controller to javafx.fxml, spring.core;

    // Export packages
    exports com.javsolutions.jw.lifeandministry;
    exports com.javsolutions.jw.lifeandministry.model;
    exports com.javsolutions.jw.lifeandministry.repository;
    exports com.javsolutions.jw.lifeandministry.config;
    exports com.javsolutions.jw.lifeandministry.controller;
    exports com.javsolutions.jw.lifeandministry.service;
}
