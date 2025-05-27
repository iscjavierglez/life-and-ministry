package com.javsolutions.jw.lifeandministry.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.Date;

/**
 * Data Transfer Object for Publisher entity.
 * Used for form binding and data transfer between view and controller.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PublisherDTO {
    private String id;
    
    @NotBlank(message = "First name is required")
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
    private String firstName;
    
    @NotBlank(message = "Last name is required")
    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
    private String lastName;
    
    @NotBlank(message = "Short name is required")
    @Size(min = 2, max = 30, message = "Short name must be between 2 and 30 characters")
    private String shortName;
    
    private String gender;
    private String privilege;
    private boolean matriculated;
    private Date dateAdded;
    private boolean active;
}
