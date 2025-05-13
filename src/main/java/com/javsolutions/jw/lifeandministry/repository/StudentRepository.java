package com.javsolutions.jw.lifeandministry.repository;

import com.javsolutions.jw.lifeandministry.model.Student;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for Student entity.
 * Spring Data MongoDB will automatically implement this interface.
 */
@Repository
public interface StudentRepository extends MongoRepository<Student, ObjectId> {

    /**
     * Find a student by email.
     * 
     * @param email the student email
     * @return the student, or null if not found
     */
    Student findByEmail(String email);

    /**
     * Find all active students.
     * 
     * @return a list of all active students
     */
    List<Student> findByActiveTrue();

    /**
     * Count active students.
     * 
     * @return the number of active students
     */
    long countByActiveTrue();

    /**
     * Update a student's active status.
     * 
     * @param id the student ID
     * @param active the new active status
     * @return the number of documents updated
     */
    @Query("{ '_id' : ?0 }")
    long updateActiveStatus(ObjectId id, boolean active);
}
