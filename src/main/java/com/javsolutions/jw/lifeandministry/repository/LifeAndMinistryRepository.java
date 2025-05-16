package com.javsolutions.jw.lifeandministry.repository;

import com.javsolutions.jw.lifeandministry.model.LifeAndMinistry;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LifeAndMinistryRepository extends MongoRepository<LifeAndMinistry, ObjectId> {
}
