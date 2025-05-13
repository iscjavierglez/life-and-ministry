package com.javsolutions.jw.lifeandministry.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.bson.types.ObjectId;

import java.util.Date;

/**
 * Model class representing a student in the Life and Ministry application.
 * This class is used to store and retrieve student data from MongoDB.
 */
@Document(collection = "students")
public class Student {
    @Id
    private ObjectId id;
    private String name;
    private String email;
    private String congregation;
    private Date dateAdded;
    private boolean active;

    /**
     * Default constructor
     */
    public Student() {
        this.dateAdded = new Date();
        this.active = true;
    }

    /**
     * Constructor with name and email
     * 
     * @param name the student's name
     * @param email the student's email
     */
    public Student(String name, String email) {
        this();
        this.name = name;
        this.email = email;
    }

    /**
     * Constructor with all fields
     * 
     * @param name the student's name
     * @param email the student's email
     * @param congregation the student's congregation
     */
    public Student(String name, String email, String congregation) {
        this(name, email);
        this.congregation = congregation;
    }

    // The toDocument and fromDocument methods are no longer needed
    // Spring Data MongoDB will handle the conversion automatically

    // Getters and Setters
    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCongregation() {
        return congregation;
    }

    public void setCongregation(String congregation) {
        this.congregation = congregation;
    }

    public Date getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(Date dateAdded) {
        this.dateAdded = dateAdded;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", congregation='" + congregation + '\'' +
                ", dateAdded=" + dateAdded +
                ", active=" + active +
                '}';
    }
}
