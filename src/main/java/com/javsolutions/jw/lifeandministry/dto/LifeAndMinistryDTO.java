package com.javsolutions.jw.lifeandministry.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Transfer Object for LifeAndMinistry entity.
 * Used for form binding and data transfer between view and controller.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LifeAndMinistryDTO {
    private String id;

    @NotBlank(message = "Month is required")
    private String month;

    @NotEmpty(message = "At least one weekly program is required")
    @Builder.Default
    private List<WeeklyProgramDTO> weeklyProgram = new ArrayList<>();
}
