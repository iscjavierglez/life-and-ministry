package com.javsolutions.jw.lifeandministry.repository;

import com.javsolutions.jw.lifeandministry.model.Publisher;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for Publisher entity.
 * Spring Data MongoDB will automatically implement this interface.
 */
@Repository
public interface PublisherRepository extends MongoRepository<Publisher, ObjectId> {

    /**
     * Find a publisher by first name and last name.
     * 
     * @param firstName the publisher's first name
     * @param lastName the publisher's last name
     * @return the publisher, or null if not found
     */
    Publisher findByFirstNameAndLastName(String firstName, String lastName);
    
    /**
     * Find a publisher by short name.
     * 
     * @param shortName the publisher's short name
     * @return the publisher, or null if not found
     */
    Publisher findByShortName(String shortName);

    /**
     * Find all active publishers.
     * 
     * @return a list of all active publishers
     */
    List<Publisher> findByActiveTrue();

    /**
     * Count active publishers.
     * 
     * @return the number of active publishers
     */
    long countByActiveTrue();

    /**
     * Find publishers by privilege.
     * 
     * @param privilege the privilege to search for
     * @return a list of publishers with the specified privilege
     */
    List<Publisher> findByPrivilege(String privilege);
    
    /**
     * Find matriculated publishers.
     * 
     * @return a list of matriculated publishers
     */
    List<Publisher> findByIsMatriculatedTrue();

    /**
     * Update a publisher's active status.
     * 
     * @param id the publisher ID
     * @param active the new active status
     * @return the number of documents updated
     */
    @Query("{ '_id' : ?0 }")
    long updateActiveStatus(ObjectId id, boolean active);
}