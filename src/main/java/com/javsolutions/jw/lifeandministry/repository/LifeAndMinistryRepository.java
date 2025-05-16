package com.javsolutions.jw.lifeandministry.repository;

import com.javsolutions.jw.lifeandministry.model.LifeAndMinistry;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface LifeAndMinistryRepository extends MongoRepository<LifeAndMinistry, ObjectId> {
    Optional<LifeAndMinistry> findByMonth(String month);
    List<LifeAndMinistry> findAllByMonth(String month);
}
