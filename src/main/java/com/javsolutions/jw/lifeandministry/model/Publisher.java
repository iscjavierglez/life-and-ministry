package com.javsolutions.jw.lifeandministry.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.bson.types.ObjectId;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

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
    private boolean isMatriculated;

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

    // Refactor constructor to use Builder pattern internally
    public Publisher(String firstName, String lastName, String shortName, String gender, String privilege, boolean isMatriculated, Date dateAdded, boolean active) {
        this(firstName, lastName, shortName, gender, privilege, isMatriculated);
        this.dateAdded = dateAdded;
        this.active = active;
    }

    // Add a constructor with fewer parameters
    public Publisher(String firstName, String lastName, String shortName, String gender, String privilege, boolean isMatriculated) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.shortName = shortName;
        this.gender = gender;
        this.privilege = privilege;
        this.isMatriculated = isMatriculated;
    }

    // Explicit getter methods for all fields
    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getShortName() {
        return shortName;
    }

    public String getPrivilege() {
        return privilege;
    }

    // Add a static builder method as a fallback
    public static PublisherBuilder builder() {
        return new PublisherBuilder();
    }

    // Inner static class for the builder
    public static class PublisherBuilder {
        private String firstName;
        private String lastName;
        private String shortName;
        private String gender;
        private String privilege;
        private boolean isMatriculated;
        private Date dateAdded = new Date();
        private boolean active = true;

        public PublisherBuilder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public PublisherBuilder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public PublisherBuilder shortName(String shortName) {
            this.shortName = shortName;
            return this;
        }

        public PublisherBuilder gender(String gender) {
            this.gender = gender;
            return this;
        }

        public PublisherBuilder privilege(String privilege) {
            this.privilege = privilege;
            return this;
        }

        public PublisherBuilder isMatriculated(boolean isMatriculated) {
            this.isMatriculated = isMatriculated;
            return this;
        }

        public PublisherBuilder dateAdded(Date dateAdded) {
            this.dateAdded = dateAdded;
            return this;
        }

        public PublisherBuilder active(boolean active) {
            this.active = active;
            return this;
        }

        public Publisher build() {
            return new Publisher(firstName, lastName, shortName, gender, privilege, isMatriculated, dateAdded, active);
        }
    }
}
