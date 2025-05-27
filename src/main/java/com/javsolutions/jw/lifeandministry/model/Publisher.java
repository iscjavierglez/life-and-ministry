package com.javsolutions.jw.lifeandministry.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * Model class representing a publisher in the Life and Ministry application.
 * This class is used to store and retrieve publisher data from MongoDB.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Slf4j
@Document(collection = "publishers")
public class Publisher {
    @Id
    private ObjectId id;
    private String firstName;
    private String lastName;
    private String shortName;
    private String gender;
    private String privilege;
    private boolean matriculated;

    @Builder.Default
    private Date dateAdded = new Date();

    @Builder.Default
    private boolean active = true;

    // Custom constructor for basic fields
    public Publisher(String firstName, String lastName, String shortName) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.shortName = shortName;
        this.dateAdded = new Date();
        this.active = true;
    }
}
