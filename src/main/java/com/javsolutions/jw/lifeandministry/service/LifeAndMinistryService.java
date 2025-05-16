package com.javsolutions.jw.lifeandministry.service;

import com.javsolutions.jw.lifeandministry.model.LifeAndMinistry;
import com.javsolutions.jw.lifeandministry.repository.LifeAndMinistryRepository;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LifeAndMinistryService {
    private final LifeAndMinistryRepository lifeAndMinistryRepository;

    public LifeAndMinistryService(LifeAndMinistryRepository lifeAndMinistryRepository) {
        this.lifeAndMinistryRepository = lifeAndMinistryRepository;
    }

    public List<LifeAndMinistry> findAll() {
        return lifeAndMinistryRepository.findAll();
    }

    public Optional<LifeAndMinistry> findById(ObjectId id) {
        return lifeAndMinistryRepository.findById(id);
    }

    public Optional<LifeAndMinistry> findByMonth(String month) {
        return lifeAndMinistryRepository.findByMonth(month);
    }

    public List<LifeAndMinistry> findAllByMonth(String month) {
        return lifeAndMinistryRepository.findAllByMonth(month);
    }

    public LifeAndMinistry save(LifeAndMinistry lifeAndMinistry) {
        return lifeAndMinistryRepository.save(lifeAndMinistry);
    }

    public LifeAndMinistry update(LifeAndMinistry lifeAndMinistry) {
        return lifeAndMinistryRepository.save(lifeAndMinistry);
    }

    public void deleteById(ObjectId id) {
        lifeAndMinistryRepository.deleteById(id);
    }
}
