package com.javsolutions.jw.lifeandministry.controller;

import com.javsolutions.jw.lifeandministry.model.Publisher;
import com.javsolutions.jw.lifeandministry.service.PublisherService;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.BindingResult;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class PublisherControllerTest {

    @Mock
    private PublisherService publisherService;

    @InjectMocks
    private PublisherController publisherController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(publisherController).build();
    }

    @Test
    void getAllPublishers_ShouldReturnAllPublishers() throws Exception {
        // Given
        List<Publisher> publishers = Arrays.asList(
                Publisher.builder().id(new ObjectId()).firstName("John").lastName("Doe").shortName("JD").build(),
                Publisher.builder().id(new ObjectId()).firstName("Jane").lastName("Smith").shortName("JS").build()
        );
        when(publisherService.findAll()).thenReturn(publishers);

        // When & Then
        mockMvc.perform(get("/publishers"))
                .andExpect(status().isOk())
                .andExpect(view().name("publishers/list"))
                .andExpect(model().attributeExists("publishers"));

        verify(publisherService).findAll();
    }

    @Test
    void newPublisherForm_ShouldReturnFormView() throws Exception {
        // When & Then
        mockMvc.perform(get("/publishers/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("publishers/form"))
                .andExpect(model().attributeExists("publisher"));
    }

    @Test
    void editPublisherForm_WithValidId_ShouldReturnFormWithPublisher() throws Exception {
        // Given
        String id = new ObjectId().toString();
        Publisher publisher = Publisher.builder()
                .id(new ObjectId(id))
                .firstName("John")
                .lastName("Doe")
                .shortName("JD")
                .build();
        when(publisherService.findById(id)).thenReturn(Optional.of(publisher));

        // When & Then
        mockMvc.perform(get("/publishers/edit/{id}", id))
                .andExpect(status().isOk())
                .andExpect(view().name("publishers/form"))
                .andExpect(model().attributeExists("publisher"));

        verify(publisherService).findById(id);
    }

    @Test
    void editPublisherForm_WithInvalidId_ShouldRedirectToList() throws Exception {
        // Given
        String id = new ObjectId().toString();
        when(publisherService.findById(id)).thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(get("/publishers/edit/{id}", id))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/publishers"));

        verify(publisherService).findById(id);
    }

    @Test
    void deletePublisher_ShouldRedirectToList() throws Exception {
        // Given
        String id = new ObjectId().toString();
        doNothing().when(publisherService).deleteById(id);

        // When & Then
        mockMvc.perform(get("/publishers/delete/{id}", id))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/publishers"))
                .andExpect(flash().attributeExists("successMessage"));

        verify(publisherService).deleteById(id);
    }

    @Test
    void togglePublisherStatus_ShouldRedirectToList() throws Exception {
        // Given
        String id = new ObjectId().toString();
        Publisher publisher = Publisher.builder()
                .id(new ObjectId(id))
                .firstName("John")
                .lastName("Doe")
                .shortName("JD")
                .active(true)
                .build();
        when(publisherService.findById(id)).thenReturn(Optional.of(publisher));
        when(publisherService.save(any(Publisher.class))).thenReturn(publisher);

        // When & Then
        mockMvc.perform(get("/publishers/toggle-status/{id}", id))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/publishers"))
                .andExpect(flash().attributeExists("successMessage"));

        verify(publisherService).findById(id);
        verify(publisherService).save(any(Publisher.class));
    }
}
