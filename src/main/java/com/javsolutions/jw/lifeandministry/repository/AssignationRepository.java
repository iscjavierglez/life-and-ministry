package com.javsolutions.jw.lifeandministry.repository;

import com.javsolutions.jw.lifeandministry.model.Assignation;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssignationRepository extends MongoRepository<Assignation, ObjectId> {
}
