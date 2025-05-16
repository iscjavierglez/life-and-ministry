package com.javsolutions.jw.lifeandministry.service;

import com.javsolutions.jw.lifeandministry.model.Assignation;
import com.javsolutions.jw.lifeandministry.model.Publisher;
import com.javsolutions.jw.lifeandministry.repository.AssignationRepository;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AssignationService {
    private final AssignationRepository assignationRepository;

    public AssignationService(AssignationRepository assignationRepository) {
        this.assignationRepository = assignationRepository;
    }

    public List<Assignation> findAll() {
        return assignationRepository.findAll();
    }

    public Optional<Assignation> findById(ObjectId id) {
        return assignationRepository.findById(id);
    }

    public Assignation save(Assignation assignation) {
        return assignationRepository.save(assignation);
    }

    public void deleteById(ObjectId id) {
        assignationRepository.deleteById(id);
    }

    public List<Assignation> findByPublisher(Publisher publisher) {
        return assignationRepository.findByPublisher(publisher);
    }

    public Assignation update(Assignation assignation) {
        return assignationRepository.save(assignation);
    }

    public List<Assignation> findByPublisherOrderByDateAsc(Publisher publisher) {
        return assignationRepository.findByPublisherOrderByDateAsc(publisher);
    }
}
