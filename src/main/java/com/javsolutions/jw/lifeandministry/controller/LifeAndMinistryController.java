package com.javsolutions.jw.lifeandministry.controller;

import com.javsolutions.jw.lifeandministry.dto.AssignationDTO;
import com.javsolutions.jw.lifeandministry.dto.LifeAndMinistryDTO;
import com.javsolutions.jw.lifeandministry.dto.PublisherDTO;
import com.javsolutions.jw.lifeandministry.dto.WeeklyProgramDTO;
import com.javsolutions.jw.lifeandministry.model.Assignation;
import com.javsolutions.jw.lifeandministry.model.AssignationType;
import com.javsolutions.jw.lifeandministry.model.LifeAndMinistry;
import com.javsolutions.jw.lifeandministry.model.Publisher;
import com.javsolutions.jw.lifeandministry.model.WeeklyProgram;
import com.javsolutions.jw.lifeandministry.service.AssignationService;
import com.javsolutions.jw.lifeandministry.service.LifeAndMinistryService;
import com.javsolutions.jw.lifeandministry.service.PublisherService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Controller for managing Life and Ministry programs.
 */
@Controller
@RequestMapping("/programs")
@Slf4j
public class LifeAndMinistryController {

    private final LifeAndMinistryService lifeAndMinistryService;
    private final PublisherService publisherService;
    private final AssignationService assignationService;

    @Autowired
    public LifeAndMinistryController(LifeAndMinistryService lifeAndMinistryService,
                                     PublisherService publisherService,
                                     AssignationService assignationService) {
        this.lifeAndMinistryService = lifeAndMinistryService;
        this.publisherService = publisherService;
        this.assignationService = assignationService;
    }

    /**
     * Display the current month's program or programs list if no current month program exists.
     *
     * @param model the model to add attributes to
     * @return the view name
     */
    @GetMapping
    public String getCurrentMonthProgram(Model model) {
        // Get current month in yyyy-MM format
        String currentMonth = YearMonth.now().format(DateTimeFormatter.ofPattern("yyyy-MM"));

        // Try to find the program for the current month
        Optional<LifeAndMinistry> currentProgram = lifeAndMinistryService.findByMonth(currentMonth);

        if (currentProgram.isPresent()) {
            // If program exists for current month, display it
            LifeAndMinistryDTO programDTO = convertToDTO(currentProgram.get());
            model.addAttribute("program", programDTO);
            model.addAttribute("currentMonth", currentMonth);
            return "programs/view";
        } else {
            // If no program exists for current month, show all programs
            return "redirect:/programs/list";
        }
    }

    /**
     * Display list of all programs.
     *
     * @param model the model to add attributes to
     * @return the view name
     */
    @GetMapping("/list")
    public String getAllPrograms(Model model) {
        List<LifeAndMinistry> programs = lifeAndMinistryService.findAll();
        List<LifeAndMinistryDTO> programDTOs = programs.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        model.addAttribute("programs", programDTOs);
        return "programs/list";
    }

    /**
     * Display a specific program by ID.
     *
     * @param id    the program ID
     * @param model the model to add attributes to
     * @return the view name
     */
    @GetMapping("/{id}")
    public String getProgramById(@PathVariable String id, Model model) {
        Optional<LifeAndMinistry> program = lifeAndMinistryService.findById(id);

        if (program.isPresent()) {
            LifeAndMinistryDTO programDTO = convertToDTO(program.get());
            model.addAttribute("program", programDTO);
            return "programs/view";
        }

        return "redirect:/programs";
    }

    /**
     * Display form for creating a new program.
     *
     * @param model the model to add attributes to
     * @return the view name
     */
    @GetMapping("/new")
    public String newProgramForm(Model model) {
        // Create a new DTO with default values
        LifeAndMinistryDTO programDTO = new LifeAndMinistryDTO();

        // Set current month as default
        String currentMonth = YearMonth.now().format(DateTimeFormatter.ofPattern("yyyy-MM"));
        programDTO.setMonth(currentMonth);

        // Add at least one weekly program
        WeeklyProgramDTO weeklyProgramDTO = new WeeklyProgramDTO();
        weeklyProgramDTO.setTitle("Week 1");
        programDTO.getWeeklyProgram().add(weeklyProgramDTO);

        model.addAttribute("program", programDTO);
        model.addAttribute("publishers", getAllActivePublisherDTOs());
        model.addAttribute("assignationTypes", AssignationType.values());

        return "programs/form";
    }

    /**
     * Handle program creation form submission.
     *
     * @param programDTO        the program data from form
     * @param bindingResult     validation results
     * @param redirectAttributes flash attributes for redirect
     * @return redirect to programs list or back to form if errors
     */
    @PostMapping
    public String createProgram(
            @Valid @ModelAttribute("program") LifeAndMinistryDTO programDTO,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("publishers", getAllActivePublisherDTOs());
            model.addAttribute("assignationTypes", AssignationType.values());
            return "programs/form";
        }

        // Check if program for this month already exists
        Optional<LifeAndMinistry> existingProgram = lifeAndMinistryService.findByMonth(programDTO.getMonth());
        if (existingProgram.isPresent()) {
            bindingResult.rejectValue("month", "error.program", 
                    "A program for this month already exists");
            model.addAttribute("publishers", getAllActivePublisherDTOs());
            model.addAttribute("assignationTypes", AssignationType.values());
            return "programs/form";
        }

        // Convert DTO to entity and save
        LifeAndMinistry program = convertToEntity(programDTO);
        lifeAndMinistryService.save(program);

        redirectAttributes.addFlashAttribute("successMessage", 
                "Program was created successfully");
        return "redirect:/programs";
    }

    /**
     * Display form for editing an existing program.
     *
     * @param id    the program ID
     * @param model the model to add attributes to
     * @return the view name
     */
    @GetMapping("/edit/{id}")
    public String editProgramForm(@PathVariable String id, Model model) {
        Optional<LifeAndMinistry> programOpt = lifeAndMinistryService.findById(id);

        if (programOpt.isPresent()) {
            LifeAndMinistryDTO programDTO = convertToDTO(programOpt.get());
            model.addAttribute("program", programDTO);
            model.addAttribute("publishers", getAllActivePublisherDTOs());
            model.addAttribute("assignationTypes", AssignationType.values());
            return "programs/form";
        }

        return "redirect:/programs";
    }

    /**
     * Handle program update form submission.
     *
     * @param id                 the program ID
     * @param programDTO         the program data from form
     * @param bindingResult      validation results
     * @param redirectAttributes flash attributes for redirect
     * @return redirect to programs list or back to form if errors
     */
    @PostMapping("/edit/{id}")
    public String updateProgram(
            @PathVariable String id,
            @Valid @ModelAttribute("program") LifeAndMinistryDTO programDTO,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("publishers", getAllActivePublisherDTOs());
            model.addAttribute("assignationTypes", AssignationType.values());
            return "programs/form";
        }

        // Check if program exists
        Optional<LifeAndMinistry> programOpt = lifeAndMinistryService.findById(id);
        if (!programOpt.isPresent()) {
            redirectAttributes.addFlashAttribute("errorMessage", 
                    "Program not found");
            return "redirect:/programs";
        }

        // Check if month was changed and if it conflicts with another program
        LifeAndMinistry existingProgram = programOpt.get();
        if (!existingProgram.getMonth().equals(programDTO.getMonth())) {
            Optional<LifeAndMinistry> conflictingProgram = lifeAndMinistryService.findByMonth(programDTO.getMonth());
            if (conflictingProgram.isPresent() && !conflictingProgram.get().getId().toString().equals(id)) {
                bindingResult.rejectValue("month", "error.program", 
                        "A program for this month already exists");
                model.addAttribute("publishers", getAllActivePublisherDTOs());
                model.addAttribute("assignationTypes", AssignationType.values());
                return "programs/form";
            }
        }

        // Convert DTO to entity and update
        LifeAndMinistry program = convertToEntity(programDTO);
        program.setId(new ObjectId(id)); // Ensure ID is set
        lifeAndMinistryService.save(program);

        redirectAttributes.addFlashAttribute("successMessage", 
                "Program was updated successfully");
        return "redirect:/programs";
    }

    /**
     * Handle program deletion.
     *
     * @param id                 the program ID
     * @param redirectAttributes flash attributes for redirect
     * @return redirect to programs list
     */
    @GetMapping("/delete/{id}")
    public String deleteProgram(
            @PathVariable String id,
            RedirectAttributes redirectAttributes) {

        try {
            lifeAndMinistryService.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage", 
                    "Program was deleted successfully");
        } catch (Exception e) {
            log.error("Error deleting program", e);
            redirectAttributes.addFlashAttribute("errorMessage", 
                    "An error occurred while deleting the program");
        }

        return "redirect:/programs";
    }

    /**
     * Add a new weekly program to an existing program.
     *
     * @param id                 the program ID
     * @param redirectAttributes flash attributes for redirect
     * @return redirect to edit program form
     */
    @GetMapping("/{id}/add-week")
    public String addWeeklyProgram(
            @PathVariable String id,
            RedirectAttributes redirectAttributes) {

        Optional<LifeAndMinistry> programOpt = lifeAndMinistryService.findById(id);
        if (programOpt.isPresent()) {
            LifeAndMinistry program = programOpt.get();

            // Create a new weekly program
            WeeklyProgram weeklyProgram = new WeeklyProgram();
            weeklyProgram.setTitle("Week " + (program.getWeeklyProgram().size() + 1));

            // Add it to the existing program
            program.getWeeklyProgram().add(weeklyProgram);
            lifeAndMinistryService.save(program);

            redirectAttributes.addFlashAttribute("successMessage", 
                    "Weekly program was added successfully");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", 
                    "Program not found");
        }

        return "redirect:/programs/edit/" + id;
    }

    /**
     * Remove a weekly program from an existing program.
     *
     * @param id        the program ID
     * @param weekIndex the index of the weekly program to remove
     * @param redirectAttributes flash attributes for redirect
     * @return redirect to edit program form
     */
    @GetMapping("/{id}/remove-week/{weekIndex}")
    public String removeWeeklyProgram(
            @PathVariable String id,
            @PathVariable int weekIndex,
            RedirectAttributes redirectAttributes) {

        Optional<LifeAndMinistry> programOpt = lifeAndMinistryService.findById(id);
        if (programOpt.isPresent()) {
            LifeAndMinistry program = programOpt.get();

            // Check if week index is valid
            if (weekIndex >= 0 && weekIndex < program.getWeeklyProgram().size()) {
                // Remove the weekly program
                program.getWeeklyProgram().remove(weekIndex);
                lifeAndMinistryService.save(program);

                redirectAttributes.addFlashAttribute("successMessage", 
                        "Weekly program was removed successfully");
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", 
                        "Invalid weekly program index");
            }
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", 
                    "Program not found");
        }

        return "redirect:/programs/edit/" + id;
    }

    /**
     * Convert LifeAndMinistry entity to DTO.
     *
     * @param program the program entity
     * @return the program DTO
     */
    private LifeAndMinistryDTO convertToDTO(LifeAndMinistry program) {
        LifeAndMinistryDTO dto = new LifeAndMinistryDTO();
        dto.setId(program.getId() != null ? program.getId().toString() : null);
        dto.setMonth(program.getMonth());

        // Convert weekly programs
        List<WeeklyProgramDTO> weeklyProgramDTOs = new ArrayList<>();
        if (program.getWeeklyProgram() != null) {
            for (WeeklyProgram weeklyProgram : program.getWeeklyProgram()) {
                weeklyProgramDTOs.add(convertToDTO(weeklyProgram));
            }
        }
        dto.setWeeklyProgram(weeklyProgramDTOs);

        return dto;
    }

    /**
     * Convert WeeklyProgram entity to DTO.
     *
     * @param weeklyProgram the weekly program entity
     * @return the weekly program DTO
     */
    private WeeklyProgramDTO convertToDTO(WeeklyProgram weeklyProgram) {
        WeeklyProgramDTO dto = new WeeklyProgramDTO();
        dto.setTitle(weeklyProgram.getTitle());
        dto.setSong1(weeklyProgram.getSong1());
        dto.setSong2(weeklyProgram.getSong2());
        dto.setSong3(weeklyProgram.getSong3());

        // Convert president assignation
        if (weeklyProgram.getPresident() != null) {
            dto.setPresident(convertToDTO(weeklyProgram.getPresident()));
        }

        // Convert treasures from God assignations
        List<AssignationDTO> treasuresFromGodDTOs = new ArrayList<>();
        if (weeklyProgram.getTreasuresFromGod() != null) {
            for (Assignation assignation : weeklyProgram.getTreasuresFromGod()) {
                treasuresFromGodDTOs.add(convertToDTO(assignation));
            }
        }
        dto.setTreasuresFromGod(treasuresFromGodDTOs);

        // Convert apply to the field ministry assignations
        List<AssignationDTO> applyToTheFieldMinistryDTOs = new ArrayList<>();
        if (weeklyProgram.getApplyToTheFieldMinistry() != null) {
            for (Assignation assignation : weeklyProgram.getApplyToTheFieldMinistry()) {
                applyToTheFieldMinistryDTOs.add(convertToDTO(assignation));
            }
        }
        dto.setApplyToTheFieldMinistry(applyToTheFieldMinistryDTOs);

        // Convert living as Christians assignations
        List<AssignationDTO> livingAsChristiansDTOs = new ArrayList<>();
        if (weeklyProgram.getLivingAsChristians() != null) {
            for (Assignation assignation : weeklyProgram.getLivingAsChristians()) {
                livingAsChristiansDTOs.add(convertToDTO(assignation));
            }
        }
        dto.setLivingAsChristians(livingAsChristiansDTOs);

        return dto;
    }

    /**
     * Convert Assignation entity to DTO.
     *
     * @param assignation the assignation entity
     * @return the assignation DTO
     */
    private AssignationDTO convertToDTO(Assignation assignation) {
        AssignationDTO dto = new AssignationDTO();
        dto.setId(assignation.getId() != null ? assignation.getId().toString() : null);
        dto.setName(assignation.getName());
        dto.setDuration(assignation.getDuration());
        dto.setType(assignation.getType());
        dto.setDate(assignation.getDate());

        // Include publisher information if available
        if (assignation.getPublisher() != null) {
            dto.setPublisherId(assignation.getPublisher().getId().toString());
            dto.setPublisherShortName(assignation.getPublisher().getShortName());
        }

        return dto;
    }

    /**
     * Convert LifeAndMinistryDTO to entity.
     *
     * @param dto the program DTO
     * @return the program entity
     */
    private LifeAndMinistry convertToEntity(LifeAndMinistryDTO dto) {
        LifeAndMinistry program = new LifeAndMinistry();
        if (dto.getId() != null && !dto.getId().isEmpty()) {
            program.setId(new ObjectId(dto.getId()));
        }
        program.setMonth(dto.getMonth());

        // Convert weekly programs
        List<WeeklyProgram> weeklyPrograms = new ArrayList<>();
        if (dto.getWeeklyProgram() != null) {
            for (WeeklyProgramDTO weeklyProgramDTO : dto.getWeeklyProgram()) {
                weeklyPrograms.add(convertToEntity(weeklyProgramDTO));
            }
        }
        program.setWeeklyProgram(weeklyPrograms);

        return program;
    }

    /**
     * Convert WeeklyProgramDTO to entity.
     *
     * @param dto the weekly program DTO
     * @return the weekly program entity
     */
    private WeeklyProgram convertToEntity(WeeklyProgramDTO dto) {
        WeeklyProgram weeklyProgram = new WeeklyProgram();
        weeklyProgram.setTitle(dto.getTitle());
        weeklyProgram.setSong1(dto.getSong1());
        weeklyProgram.setSong2(dto.getSong2());
        weeklyProgram.setSong3(dto.getSong3());

        // Convert president assignation
        if (dto.getPresident() != null) {
            weeklyProgram.setPresident(convertToEntity(dto.getPresident()));
        }

        // Convert treasures from God assignations
        List<Assignation> treasuresFromGod = new ArrayList<>();
        if (dto.getTreasuresFromGod() != null) {
            for (AssignationDTO assignationDTO : dto.getTreasuresFromGod()) {
                treasuresFromGod.add(convertToEntity(assignationDTO));
            }
        }
        weeklyProgram.setTreasuresFromGod(treasuresFromGod);

        // Convert apply to the field ministry assignations
        List<Assignation> applyToTheFieldMinistry = new ArrayList<>();
        if (dto.getApplyToTheFieldMinistry() != null) {
            for (AssignationDTO assignationDTO : dto.getApplyToTheFieldMinistry()) {
                applyToTheFieldMinistry.add(convertToEntity(assignationDTO));
            }
        }
        weeklyProgram.setApplyToTheFieldMinistry(applyToTheFieldMinistry);

        // Convert living as Christians assignations
        List<Assignation> livingAsChristians = new ArrayList<>();
        if (dto.getLivingAsChristians() != null) {
            for (AssignationDTO assignationDTO : dto.getLivingAsChristians()) {
                livingAsChristians.add(convertToEntity(assignationDTO));
            }
        }
        weeklyProgram.setLivingAsChristians(livingAsChristians);

        return weeklyProgram;
    }

    /**
     * Convert AssignationDTO to entity.
     *
     * @param dto the assignation DTO
     * @return the assignation entity
     */
    private Assignation convertToEntity(AssignationDTO dto) {
        Assignation assignation = new Assignation();
        if (dto.getId() != null && !dto.getId().isEmpty()) {
            assignation.setId(new ObjectId(dto.getId()));
        }
        assignation.setName(dto.getName());
        assignation.setDuration(dto.getDuration());
        assignation.setType(dto.getType());
        assignation.setDate(dto.getDate());

        // Include publisher if ID is provided
        if (dto.getPublisherId() != null && !dto.getPublisherId().isEmpty()) {
            Optional<Publisher> publisher = publisherService.findById(dto.getPublisherId());
            publisher.ifPresent(assignation::setPublisher);
        }

        return assignation;
    }

    /**
     * Get all active publishers as DTOs.
     *
     * @return list of active publisher DTOs
     */
    private List<PublisherDTO> getAllActivePublisherDTOs() {
        return publisherService.findActivePublishers().stream()
                .map(publisher -> PublisherDTO.builder()
                        .id(publisher.getId().toString())
                        .firstName(publisher.getFirstName())
                        .lastName(publisher.getLastName())
                        .shortName(publisher.getShortName())
                        .build())
                .collect(Collectors.toList());
    }

    /**
     * Add common model attributes for forms.
     *
     * @param model the model to add attributes to
     */
    @ModelAttribute
    public void addCommonAttributes(Model model) {
        model.addAttribute("months", getAvailableMonths());
        model.addAttribute("currentMonth", YearMonth.now().format(DateTimeFormatter.ofPattern("yyyy-MM")));
    }

    /**
     * Get available months for selection.
     *
     * @return list of available months in yyyy-MM format
     */
    private List<String> getAvailableMonths() {
        List<String> months = new ArrayList<>();
        YearMonth current = YearMonth.now();

        // Add 6 months before current month
        for (int i = -6; i <= 6; i++) {
            YearMonth yearMonth = current.plusMonths(i);
            months.add(yearMonth.format(DateTimeFormatter.ofPattern("yyyy-MM")));
        }

        return months;
    }
}
