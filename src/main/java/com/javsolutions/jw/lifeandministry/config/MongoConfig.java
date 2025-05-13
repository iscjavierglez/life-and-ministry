package com.javsolutions.jw.lifeandministry.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * MongoDB configuration class.
 * This class enables MongoDB repositories and configures any additional MongoDB settings.
 */
@Configuration
@EnableMongoRepositories(basePackages = "com.javsolutions.jw.lifeandministry.repository")
public class MongoConfig {
    // The configuration is done through application.properties
    // This class is just to enable MongoDB repositories
}