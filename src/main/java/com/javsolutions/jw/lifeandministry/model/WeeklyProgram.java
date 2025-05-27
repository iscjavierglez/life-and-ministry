package com.javsolutions.jw.lifeandministry.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WeeklyProgram {
    private String title;
    private Assignation president;
    private String song1;
    private String song2;
    private String song3;
    @Builder.Default
    private List<Assignation> treasuresFromGod = new java.util.ArrayList<>();
    @Builder.Default
    private List<Assignation> applyToTheFieldMinistry = new java.util.ArrayList<>();
    @Builder.Default
    private List<Assignation> livingAsChristians = new java.util.ArrayList<>();
}
