package com.javsolutions.jw.lifeandministry.service;

import com.javsolutions.jw.lifeandministry.model.Publisher;
import com.javsolutions.jw.lifeandministry.repository.PublisherRepository;
import org.springframework.stereotype.Service;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.Optional;

@Service
public class PublisherService {

    private final PublisherRepository publisherRepository;

    public PublisherService(PublisherRepository publisherRepository) {
        this.publisherRepository = publisherRepository;
    }

    public List<Publisher> findAll() {
        return publisherRepository.findAll();
    }

    public Optional<Publisher> findById(String id) {
        ObjectId objectId = new ObjectId(id);
        return publisherRepository.findById(objectId);
    }

    public Publisher save(Publisher publisher) {
        return publisherRepository.save(publisher);
    }

    public void deleteById(String id) {
        ObjectId objectId = new ObjectId(id);
        publisherRepository.deleteById(objectId);
    }
}
