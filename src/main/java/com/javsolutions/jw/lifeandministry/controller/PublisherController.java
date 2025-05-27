package com.javsolutions.jw.lifeandministry.controller;

import com.javsolutions.jw.lifeandministry.dto.PublisherDTO;
import com.javsolutions.jw.lifeandministry.model.Publisher;
import com.javsolutions.jw.lifeandministry.service.PublisherService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Controller for managing Publishers.
 */
@Controller
@RequestMapping("/publishers")
@Slf4j
public class PublisherController {

    private final PublisherService publisherService;

    @Autowired
    public PublisherController(PublisherService publisherService) {
        this.publisherService = publisherService;
    }

    /**
     * Display list of all publishers with optional filtering.
     *
     * @param active     filter by active status if provided
     * @param privilege  filter by privilege if provided
     * @param model      the model to add attributes to
     * @return the view name
     */
    @GetMapping
    public String getAllPublishers(
            @RequestParam(required = false) Boolean active,
            @RequestParam(required = false) String privilege,
            Model model) {
        
        List<Publisher> publishers;
        
        if (active != null && active) {
            publishers = publisherService.findActivePublishers();
            model.addAttribute("activeFilter", true);
        } else if (privilege != null && !privilege.isEmpty()) {
            publishers = publisherService.findByPrivilege(privilege);
            model.addAttribute("privilegeFilter", privilege);
        } else {
            publishers = publisherService.findAll();
        }
        
        // Convert entities to DTOs
        List<PublisherDTO> publisherDTOs = publishers.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        
        model.addAttribute("publishers", publisherDTOs);
        return "publishers/list";
    }

    /**
     * Display form for creating a new publisher.
     *
     * @param model the model to add attributes to
     * @return the view name
     */
    @GetMapping("/new")
    public String newPublisherForm(Model model) {
        model.addAttribute("publisher", new PublisherDTO());
        return "publishers/form";
    }

    /**
     * Handle publisher creation form submission.
     *
     * @param publisherDTO        the publisher data from form
     * @param bindingResult       validation results
     * @param redirectAttributes  flash attributes for redirect
     * @return redirect to publishers list or back to form if errors
     */
    @PostMapping
    public String createPublisher(
            @Valid @ModelAttribute("publisher") PublisherDTO publisherDTO,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {
        
        if (bindingResult.hasErrors()) {
            return "publishers/form";
        }
        
        // Check if publisher with same shortName already exists
        Publisher existingPublisher = publisherService.findByShortName(publisherDTO.getShortName());
        if (existingPublisher != null) {
            bindingResult.rejectValue("shortName", "error.publisher", 
                    "A publisher with this short name already exists");
            return "publishers/form";
        }
        
        Publisher publisher = convertToEntity(publisherDTO);
        publisher.setDateAdded(new Date());
        publisherService.save(publisher);
        
        redirectAttributes.addFlashAttribute("successMessage", 
                "Publisher was created successfully");
        return "redirect:/publishers";
    }

    /**
     * Display form for editing an existing publisher.
     *
     * @param id    the publisher ID
     * @param model the model to add attributes to
     * @return the view name
     */
    @GetMapping("/edit/{id}")
    public String editPublisherForm(@PathVariable String id, Model model) {
        Optional<Publisher> publisherOpt = publisherService.findById(id);
        
        if (publisherOpt.isPresent()) {
            PublisherDTO publisherDTO = convertToDTO(publisherOpt.get());
            model.addAttribute("publisher", publisherDTO);
            return "publishers/form";
        }
        
        return "redirect:/publishers";
    }

    /**
     * Handle publisher update form submission.
     *
     * @param id                  the publisher ID
     * @param publisherDTO        the publisher data from form
     * @param bindingResult       validation results
     * @param redirectAttributes  flash attributes for redirect
     * @return redirect to publishers list or back to form if errors
     */
    @PostMapping("/edit/{id}")
    public String updatePublisher(
            @PathVariable String id,
            @Valid @ModelAttribute("publisher") PublisherDTO publisherDTO,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {
        
        if (bindingResult.hasErrors()) {
            return "publishers/form";
        }
        
        // Check if publisher with same shortName already exists (but not this one)
        Publisher existingPublisher = publisherService.findByShortName(publisherDTO.getShortName());
        if (existingPublisher != null && !existingPublisher.getId().toString().equals(id)) {
            bindingResult.rejectValue("shortName", "error.publisher", 
                    "A publisher with this short name already exists");
            return "publishers/form";
        }
        
        Optional<Publisher> publisherOpt = publisherService.findById(id);
        if (publisherOpt.isPresent()) {
            Publisher publisher = publisherOpt.get();
            
            // Update fields but preserve dateAdded
            Date originalDateAdded = publisher.getDateAdded();
            publisher = convertToEntity(publisherDTO);
            publisher.setDateAdded(originalDateAdded);
            
            publisherService.save(publisher);
            redirectAttributes.addFlashAttribute("successMessage", 
                    "Publisher was updated successfully");
        }
        
        return "redirect:/publishers";
    }

    /**
     * Handle publisher deletion.
     *
     * @param id                  the publisher ID
     * @param redirectAttributes  flash attributes for redirect
     * @return redirect to publishers list
     */
    @GetMapping("/delete/{id}")
    public String deletePublisher(
            @PathVariable String id,
            RedirectAttributes redirectAttributes) {
        
        try {
            publisherService.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage", 
                    "Publisher was deleted successfully");
        } catch (Exception e) {
            log.error("Error deleting publisher", e);
            redirectAttributes.addFlashAttribute("errorMessage", 
                    "An error occurred while deleting the publisher");
        }
        
        return "redirect:/publishers";
    }

    /**
     * Toggle publisher active status.
     *
     * @param id                  the publisher ID
     * @param redirectAttributes  flash attributes for redirect
     * @return redirect to publishers list
     */
    @GetMapping("/toggle-status/{id}")
    public String togglePublisherStatus(
            @PathVariable String id,
            RedirectAttributes redirectAttributes) {
        
        Optional<Publisher> publisherOpt = publisherService.findById(id);
        if (publisherOpt.isPresent()) {
            Publisher publisher = publisherOpt.get();
            boolean newStatus = !publisher.isActive();
            
            publisher.setActive(newStatus);
            publisherService.save(publisher);
            
            String statusMessage = newStatus ? "activated" : "deactivated";
            redirectAttributes.addFlashAttribute("successMessage", 
                    "Publisher was " + statusMessage + " successfully");
        }
        
        return "redirect:/publishers";
    }

    /**
     * Convert Publisher entity to PublisherDTO.
     *
     * @param publisher the publisher entity
     * @return the publisher DTO
     */
    private PublisherDTO convertToDTO(Publisher publisher) {
        return PublisherDTO.builder()
                .id(publisher.getId() != null ? publisher.getId().toString() : null)
                .firstName(publisher.getFirstName())
                .lastName(publisher.getLastName())
                .shortName(publisher.getShortName())
                .gender(publisher.getGender())
                .privilege(publisher.getPrivilege())
                .matriculated(publisher.isMatriculated())
                .dateAdded(publisher.getDateAdded())
                .active(publisher.isActive())
                .build();
    }

    /**
     * Convert PublisherDTO to Publisher entity.
     *
     * @param publisherDTO the publisher DTO
     * @return the publisher entity
     */
    private Publisher convertToEntity(PublisherDTO publisherDTO) {
        return Publisher.builder()
                .id(publisherDTO.getId() != null && !publisherDTO.getId().isEmpty() ? 
                        new org.bson.types.ObjectId(publisherDTO.getId()) : null)
                .firstName(publisherDTO.getFirstName())
                .lastName(publisherDTO.getLastName())
                .shortName(publisherDTO.getShortName())
                .gender(publisherDTO.getGender())
                .privilege(publisherDTO.getPrivilege())
                .matriculated(publisherDTO.isMatriculated())
                .dateAdded(publisherDTO.getDateAdded())
                .active(publisherDTO.isActive())
                .build();
    }

    /**
     * Add common model attributes for forms.
     *
     * @param model the model to add attributes to
     */
    @ModelAttribute
    public void addCommonAttributes(Model model) {
        // Add gender options
        model.addAttribute("genderOptions", List.of("Male", "Female"));
        
        // Add privilege options
        model.addAttribute("privilegeOptions", List.of(
                "PUBLISHER", 
                "AUXILIARY_PIONEER", 
                "REGULAR_PIONEER", 
                "MINISTERIAL_SERVANT", 
                "ELDER"));
    }
}
