package com.javsolutions.jw.lifeandministry.service;

import com.javsolutions.jw.lifeandministry.model.Assignation;
import com.javsolutions.jw.lifeandministry.model.AssignationType;
import com.javsolutions.jw.lifeandministry.model.Publisher;
import com.javsolutions.jw.lifeandministry.repository.AssignationRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Service for managing assignations.
 */
@Service
public class AssignationService {

    private final AssignationRepository assignationRepository;
    private final PublisherService publisherService;

    @Autowired
    public AssignationService(AssignationRepository assignationRepository, PublisherService publisherService) {
        this.assignationRepository = assignationRepository;
        this.publisherService = publisherService;
    }

    /**
     * Find all assignations.
     *
     * @return list of all assignations
     */
    public List<Assignation> findAll() {
        return assignationRepository.findAll();
    }

    /**
     * Find assignation by ID.
     *
     * @param id the assignation ID
     * @return the assignation if found
     */
    public Optional<Assignation> findById(String id) {
        return assignationRepository.findById(new ObjectId(id));
    }

    /**
     * Find assignations by publisher ID.
     *
     * @param publisherId the publisher ID
     * @return list of assignations for the specified publisher
     */
    public List<Assignation> findByPublisherId(String publisherId) {
        Optional<Publisher> publisher = publisherService.findById(publisherId);
        return publisher.map(assignationRepository::findByPublisher)
                .orElse(List.of());
    }

    /**
     * Save a new assignation or update an existing one.
     *
     * @param assignation the assignation to save
     * @return the saved assignation
     */
    public Assignation save(Assignation assignation) {
        return assignationRepository.save(assignation);
    }

    /**
     * Delete an assignation by ID.
     *
     * @param id the assignation ID
     */
    public void deleteById(String id) {
        assignationRepository.deleteById(new ObjectId(id));
    }

    /**
     * Create a new assignation with a publisher.
     *
     * @param name        the assignation name
     * @param duration    the duration in minutes
     * @param type        the assignation type
     * @param date        the assignation date
     * @param publisherId the publisher ID
     * @return the created assignation
     */
    public Assignation createAssignation(String name, int duration, AssignationType type, 
                                         LocalDate date, String publisherId) {
        Assignation assignation = new Assignation();
        assignation.setName(name);
        assignation.setDuration(duration);
        assignation.setType(type);
        assignation.setDate(date);

        if (publisherId != null && !publisherId.isEmpty()) {
            Optional<Publisher> publisher = publisherService.findById(publisherId);
            publisher.ifPresent(assignation::setPublisher);
        }

        return assignationRepository.save(assignation);
    }
}
