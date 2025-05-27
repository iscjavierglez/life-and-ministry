package com.javsolutions.jw.lifeandministry.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.bson.types.ObjectId;
import java.util.List;

/**
 * Model class representing a Life and Ministry meeting.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "life_and_ministry")
public class LifeAndMinistry {
    @Id
    private ObjectId id;
    private String month;
    private List<WeeklyProgram> weeklyProgram;
}
