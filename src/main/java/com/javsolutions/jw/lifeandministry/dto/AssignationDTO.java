package com.javsolutions.jw.lifeandministry.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.javsolutions.jw.lifeandministry.model.AssignationType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

/**
 * Data Transfer Object for Assignation entity.
 * Used for form binding and data transfer between view and controller.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssignationDTO {
    private String id;

    @NotBlank(message = "Name is required")
    private String name;

    @Min(value = 1, message = "Duration must be at least 1 minute")
    private int duration; // in minutes

    @NotNull(message = "Type is required")
    private AssignationType type;

    private LocalDate date;

    private String publisherId;
    private String publisherShortName;
}
