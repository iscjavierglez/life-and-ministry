package com.javsolutions.jw.lifeandministry.service;

import com.javsolutions.jw.lifeandministry.model.Publisher;
import com.javsolutions.jw.lifeandministry.repository.PublisherRepository;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;

/**
 * Service class for managing Publisher entities.
 */
@Service
@Slf4j
public class PublisherService {

    private final PublisherRepository publisherRepository;

    public PublisherService(PublisherRepository publisherRepository) {
        this.publisherRepository = publisherRepository;
    }

    /**
     * Find all publishers.
     *
     * @return a list of all publishers
     */
    public List<Publisher> findAll() {
        log.info("Fetching all publishers");
        return publisherRepository.findAll();
    }

    /**
     * Find publisher by ID.
     *
     * @param id the publisher ID
     * @return an Optional containing the publisher if found
     */
    public Optional<Publisher> findById(String id) {
        log.info("Fetching publisher with ID: {}", id);
        ObjectId objectId = new ObjectId(id);
        return publisherRepository.findById(objectId);
    }

    /**
     * Save a publisher.
     *
     * @param publisher the publisher to save
     * @return the saved publisher
     */
    public Publisher save(Publisher publisher) {
        log.info("Saving publisher: {}", publisher.getShortName());
        return publisherRepository.save(publisher);
    }

    /**
     * Delete a publisher by ID.
     *
     * @param id the publisher ID
     */
    public void deleteById(String id) {
        log.info("Deleting publisher with ID: {}", id);
        ObjectId objectId = new ObjectId(id);
        publisherRepository.deleteById(objectId);
    }

    /**
     * Find only active publishers.
     *
     * @return a list of active publishers
     */
    public List<Publisher> findActivePublishers() {
        log.info("Fetching active publishers");
        return publisherRepository.findByActiveTrue();
    }

    /**
     * Find publishers by privilege.
     *
     * @param privilege the privilege to filter by
     * @return a list of publishers with the specified privilege
     */
    public List<Publisher> findByPrivilege(String privilege) {
        log.info("Fetching publishers with privilege: {}", privilege);
        return publisherRepository.findByPrivilege(privilege);
    }

    /**
     * Find publishers by first and last name.
     *
     * @param firstName the first name
     * @param lastName the last name
     * @return the publisher if found, or null
     */
    public Publisher findByName(String firstName, String lastName) {
        log.info("Fetching publisher with name: {} {}", firstName, lastName);
        return publisherRepository.findByFirstNameAndLastName(firstName, lastName);
    }

    /**
     * Find publisher by short name.
     *
     * @param shortName the short name
     * @return the publisher if found, or null
     */
    public Publisher findByShortName(String shortName) {
        log.info("Fetching publisher with short name: {}", shortName);
        return publisherRepository.findByShortName(shortName);
    }

    /**
     * Find matriculated publishers.
     *
     * @return a list of matriculated publishers
     */
    public List<Publisher> findMatriculatedPublishers() {
        log.info("Fetching matriculated publishers");
        return publisherRepository.findByMatriculatedTrue();
    }

    /**
     * Update a publisher's active status.
     *
     * @param id the publisher ID
     * @param active the new active status
     * @return true if update was successful
     */
    public boolean updateActiveStatus(String id, boolean active) {
        log.info("Updating active status for publisher ID: {} to {}", id, active);
        ObjectId objectId = new ObjectId(id);
        return publisherRepository.updateActiveStatus(objectId, active) > 0;
    }
}
