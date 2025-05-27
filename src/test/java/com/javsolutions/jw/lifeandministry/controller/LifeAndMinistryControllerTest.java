package com.javsolutions.jw.lifeandministry.controller;

import com.javsolutions.jw.lifeandministry.model.LifeAndMinistry;
import com.javsolutions.jw.lifeandministry.model.Publisher;
import com.javsolutions.jw.lifeandministry.model.WeeklyProgram;
import com.javsolutions.jw.lifeandministry.service.AssignationService;
import com.javsolutions.jw.lifeandministry.service.LifeAndMinistryService;
import com.javsolutions.jw.lifeandministry.service.PublisherService;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class LifeAndMinistryControllerTest {

    @Mock
    private LifeAndMinistryService lifeAndMinistryService;

    @Mock
    private PublisherService publisherService;

    @Mock
    private AssignationService assignationService;

    @InjectMocks
    private LifeAndMinistryController lifeAndMinistryController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(lifeAndMinistryController).build();
    }

    @Test
    void getCurrentMonthProgram_WithExistingProgram_ShouldDisplayIt() throws Exception {
        // Given
        String currentMonth = YearMonth.now().format(DateTimeFormatter.ofPattern("yyyy-MM"));
        LifeAndMinistry program = new LifeAndMinistry();
        program.setId(new ObjectId());
        program.setMonth(currentMonth);
        program.setWeeklyProgram(List.of(new WeeklyProgram()));

        when(lifeAndMinistryService.findByMonth(currentMonth)).thenReturn(Optional.of(program));

        // When & Then
        mockMvc.perform(get("/programs"))
                .andExpect(status().isOk())
                .andExpect(view().name("programs/view"))
                .andExpect(model().attributeExists("program"))
                .andExpect(model().attributeExists("currentMonth"));
    }

    @Test
    void getCurrentMonthProgram_WithNoExistingProgram_ShouldRedirectToList() throws Exception {
        // Given
        String currentMonth = YearMonth.now().format(DateTimeFormatter.ofPattern("yyyy-MM"));
        when(lifeAndMinistryService.findByMonth(currentMonth)).thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(get("/programs"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/programs/list"));
    }

    @Test
    void getAllPrograms_ShouldReturnAllPrograms() throws Exception {
        // Given
        List<LifeAndMinistry> programs = Arrays.asList(
                createTestProgram("2025-01"),
                createTestProgram("2025-02")
        );
        when(lifeAndMinistryService.findAll()).thenReturn(programs);

        // When & Then
        mockMvc.perform(get("/programs/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("programs/list"))
                .andExpect(model().attributeExists("programs"));
    }

    @Test
    void getProgramById_WithValidId_ShouldDisplayProgram() throws Exception {
        // Given
        String id = new ObjectId().toString();
        LifeAndMinistry program = createTestProgram("2025-01");
        program.setId(new ObjectId(id));

        when(lifeAndMinistryService.findById(id)).thenReturn(Optional.of(program));

        // When & Then
        mockMvc.perform(get("/programs/{id}", id))
                .andExpect(status().isOk())
                .andExpect(view().name("programs/view"))
                .andExpect(model().attributeExists("program"));
    }

    @Test
    void getProgramById_WithInvalidId_ShouldRedirectToPrograms() throws Exception {
        // Given
        String id = new ObjectId().toString();
        when(lifeAndMinistryService.findById(id)).thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(get("/programs/{id}", id))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/programs"));
    }

    @Test
    void newProgramForm_ShouldReturnFormView() throws Exception {
        // Given
        List<Publisher> publishers = Arrays.asList(
                createTestPublisher("John", "Doe", "JD"),
                createTestPublisher("Jane", "Smith", "JS")
        );
        when(publisherService.findActivePublishers()).thenReturn(publishers);

        // When & Then
        mockMvc.perform(get("/programs/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("programs/form"))
                .andExpect(model().attributeExists("program"))
                .andExpect(model().attributeExists("publishers"))
                .andExpect(model().attributeExists("assignationTypes"));
    }

    private LifeAndMinistry createTestProgram(String month) {
        LifeAndMinistry program = new LifeAndMinistry();
        program.setId(new ObjectId());
        program.setMonth(month);
        program.setWeeklyProgram(List.of(new WeeklyProgram()));
        return program;
    }

    private Publisher createTestPublisher(String firstName, String lastName, String shortName) {
        Publisher publisher = new Publisher();
        publisher.setId(new ObjectId());
        publisher.setFirstName(firstName);
        publisher.setLastName(lastName);
        publisher.setShortName(shortName);
        publisher.setActive(true);
        return publisher;
    }
}
