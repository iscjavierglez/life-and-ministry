package com.javsolutions.jw.lifeandministry.repository;

import com.javsolutions.jw.lifeandministry.model.Assignation;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import com.javsolutions.jw.lifeandministry.model.Publisher;

@Repository
public interface AssignationRepository extends MongoRepository<Assignation, ObjectId> {
    List<Assignation> findByPublisher(Publisher publisher);
}
