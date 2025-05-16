package com.javsolutions.jw.lifeandministry.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import java.util.Date;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.bson.types.ObjectId;

/**
 * Model class representing an assignation for a publisher.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "assignations")
public class Assignation {
    @Id
    private ObjectId id;
    private String type;
    private String name;
    private Date date;
}
