package com.javsolutions.jw.lifeandministry.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Transfer Object for WeeklyProgram entity.
 * Used for form binding and data transfer between view and controller.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WeeklyProgramDTO {
    @NotBlank(message = "Title is required")
    private String title;

    private AssignationDTO president;

    private String song1;
    private String song2;
    private String song3;

    @Builder.Default
    private List<AssignationDTO> treasuresFromGod = new ArrayList<>();

    @Builder.Default
    private List<AssignationDTO> applyToTheFieldMinistry = new ArrayList<>();

    @Builder.Default
    private List<AssignationDTO> livingAsChristians = new ArrayList<>();
}
