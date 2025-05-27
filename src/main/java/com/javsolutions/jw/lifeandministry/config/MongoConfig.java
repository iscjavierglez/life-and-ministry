package com.javsolutions.jw.lifeandministry.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.Collection;
import java.util.Collections;

/**
 * MongoDB configuration class.
 * This class enables MongoDB repositories and configures any additional MongoDB settings.
 */
@Configuration
@EnableMongoRepositories(basePackages = "com.javsolutions.jw.lifeandministry.repository")
public class MongoConfig extends AbstractMongoClientConfiguration {

    @Value("${spring.data.mongodb.uri}")
    private String connectionString;

    @Override
    public MongoClient mongoClient() {
        ConnectionString connection = new ConnectionString(connectionString);
        MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
                .applyConnectionString(connection)
                .build();
        return MongoClients.create(mongoClientSettings);
    }

    @Override
    protected String getDatabaseName() {
        return "jwcluster";
    }

    @Override
    public Collection<String> getMappingBasePackages() {
        return Collections.singleton("com.javsolutions.jw.lifeandministry");
    }
}