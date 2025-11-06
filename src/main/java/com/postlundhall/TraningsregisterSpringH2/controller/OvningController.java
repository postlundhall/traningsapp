package com.postlundhall.TraningsregisterSpringH2.controller;

import com.postlundhall.TraningsregisterSpringH2.model.Ovning;
import com.postlundhall.TraningsregisterSpringH2.service.OvningService;
import jakarta.persistence.OptimisticLockException;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

/**
 * Controller for {@link Ovning} CRUD management with form validation and flash messages.
 * Handles CRUD operations with POST for create (/ovningar), edit (/ovningar/{id}), and delete (/ovningar/{id}/delete).
 * Uses ovningar/error.html for entity-specific errors and defers global errors (e.g., 403, 404, 500) to GlobalExceptionHandler.
 * @author postlundhall
 * @since 1.0
 */
@Controller
@RequestMapping("/ovningar")
public class OvningController {
    private static final Logger logger = LoggerFactory.getLogger(OvningController.class);

    private final OvningService ovningService;
    private static final String ENTITY_ATTR_NAME = "ovning";  // Singular for th:object
    private static final String LIST_ATTR_NAME = "ovningar"; // Plural for list
    private static final String VIEW_FOLDER = "ovningar";

    @Autowired
    public OvningController(OvningService ovningService) {
        this.ovningService = ovningService;
    }

    /**
     * Fetches {@code Ovning} entities from the database
     * according to optional filtering, sorting and pagination criteria.
     * After being fetched, they are added to the MVC model for display in a Thymeleaf list view.
     *
     * @param search Ovningsnamn search term (partial match).
     * @param equipment Equipment enum value.
     * @param muscle Primary muscle enum value.
     * @param sortBy Property to sort the list by (default: ovningsnamn).
     * @param sortDir Sort direction (asc/desc, default: asc).
     * @param page Current page number (0-based, default: 0).
     * @param size Items per page (default: 10).
     * @param model the Spring MVC model used to pass entity attributes to the view.
     * @return Name of the Thymeleaf-template that will display the list (ovningar/list).
     */
    @GetMapping
    public String list(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String equipment,
            @RequestParam(required = false) String muscle,
            @RequestParam(required = false, defaultValue = "ovningsnamn") String sortBy,
            @RequestParam(required = false, defaultValue = "asc") String sortDir,
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int size,
            Model model) {
        Page<Ovning> ovningarPage = ovningService.findAllWithFiltersAndSort(search, equipment, muscle, sortBy, sortDir, page, size);
        model.addAttribute(LIST_ATTR_NAME, ovningarPage.getContent());
        model.addAttribute("search", search);
        model.addAttribute("equipment", equipment);
        model.addAttribute("muscle", muscle);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("currentPage", ovningarPage.getNumber());
        model.addAttribute("totalPages", ovningarPage.getTotalPages());
        model.addAttribute("totalItems", ovningarPage.getTotalElements());
        model.addAttribute("size", size);
        return VIEW_FOLDER + "/list";
    }

    /**
     * Displays the form for creating a new exercise.
     *
     * @param model The model to pass attributes to the view.
     * @return The name of the form template (ovningar/form).
     */
    @GetMapping("/new")
    public String createForm(Model model) {
        if (!model.containsAttribute(ENTITY_ATTR_NAME)) {
            model.addAttribute(ENTITY_ATTR_NAME, new Ovning());
        }
        return VIEW_FOLDER + "/form";
    }

    /**
     * Displays the form for editing an existing exercise.
     *
     * @param id    The ID of the exercise to edit.
     * @param model The model to pass attributes to the view.
     * @return The name of the form template (ovningar/form) or error template (ovningar/error) if not found.
     */
    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Ovning> ovningOpt = ovningService.findById(id);
        if (ovningOpt.isPresent()) {
            model.addAttribute(ENTITY_ATTR_NAME, ovningOpt.get());
            return VIEW_FOLDER + "/form";
        } else {
            logger.warn("Ovning not found for edit, ID: {}", id);
            redirectAttributes.addFlashAttribute("errorMessage", "ovning.error.notfound");
            redirectAttributes.addFlashAttribute("entityId", id);
            return "redirect:/" + VIEW_FOLDER + "/error";
        }
    }

    /**
     * Creates an Ovning consisting of Ovning-attributes.
     * Uses flash message to provide feedback if Ovning creation was successful, or an error message if it was not.
     *
     * @param ovning             The exercise object from the create-form.
     * @param result             The validation result.
     * @param model              The model for error handling.
     * @param redirectAttributes For flash messages on redirect.
     * @return Redirect to list on success, or form template on error.
     */
    @PostMapping
    public String create(@Valid @ModelAttribute Ovning ovning, BindingResult result, Model model,
                         RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute(ENTITY_ATTR_NAME, ovning);
            return VIEW_FOLDER + "/form";
        }
        try {
            ovningService.save(ovning);
            redirectAttributes.addFlashAttribute("successMessage", "ovning.success.created");
            return "redirect:/" + VIEW_FOLDER;
        } catch (IllegalArgumentException e) {
            logger.error("Create failed for Ovning: {}", ovning, e);
            model.addAttribute(ENTITY_ATTR_NAME, ovning);
            model.addAttribute("errorMessage", e.getMessage());
            return VIEW_FOLDER + "/form";
        } catch (Exception e) {
            logger.error("Unexpected error in create for Ovning: {}", ovning, e);
            model.addAttribute(ENTITY_ATTR_NAME, ovning);
            model.addAttribute("errorMessage", "ovning.error.unexpected");
            return VIEW_FOLDER + "/form";
        }
    }

    /**
     * Edits attributes of an existing Ovning.
     * Uses flash message to provide feedback if the edit was successful, or an error message if it was not.
     *
     * @param id                 The ID of the exercise to edit.
     * @param ovning             The Ovning object from the edit-form.
     * @param result             The validation result.
     * @param model              The model for error handling.
     * @param redirectAttributes For flash messages on redirect.
     * @return Redirect to list on success, or form template on error.
     */
    @PostMapping("/{id}")
    public String edit(@PathVariable Long id, @Valid @ModelAttribute Ovning ovning, BindingResult result,
                         Model model, RedirectAttributes redirectAttributes) {
        if (!id.equals(ovning.getId())) {
            logger.warn("ID mismatch: PathVariable {} != Ovning ID {}", id, ovning.getId());
            model.addAttribute("errorMessage", "ovning.error.id.mismatch");
            model.addAttribute(ENTITY_ATTR_NAME, ovning);
            return VIEW_FOLDER + "/form";
        }
        if (result.hasErrors()) {
            logger.debug("Validation errors for Ovning ID {}: {}", id, result.getAllErrors());
            model.addAttribute(ENTITY_ATTR_NAME, ovning);
            return VIEW_FOLDER + "/form";
        }
        try {
            ovningService.save(ovning);
            redirectAttributes.addFlashAttribute("successMessage", "ovning.success.edited");
            return "redirect:/" + VIEW_FOLDER;
        } catch (OptimisticLockException e) {
            logger.error("Optimistic lock failure for Ovning ID {}: ", id, e);
            model.addAttribute(ENTITY_ATTR_NAME, ovning);
            model.addAttribute("errorMessage", "ovning.error.optimistic.lock");
            return VIEW_FOLDER + "/form";
        } catch (IllegalArgumentException e) {
            logger.error("Illegal argument in edit for Ovning ID {}: ", id, e);
            model.addAttribute(ENTITY_ATTR_NAME, ovning);
            model.addAttribute("errorMessage", e.getMessage());
            return VIEW_FOLDER + "/form";
        } catch (Exception e) {
            logger.error("Unexpected error in edit for Ovning ID {}: ", id, e);
            model.addAttribute(ENTITY_ATTR_NAME, ovning);
            model.addAttribute("errorMessage", "ovning.error.unexpected");
            return VIEW_FOLDER + "/form";
        }
    }

    /**
     * Deletes an existing Ovning by ID.
     * Uses flash message to provide feedback if the Ovning was successfully deleted, or an error message if it was not.
     * @param id                 The ID of the Ovning to delete.
     * @param redirectAttributes Flash message on redirect.
     * @return Redirect to list page.
     */
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            ovningService.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage", "ovning.success.deleted");
        } catch (IllegalArgumentException e) {
            logger.error("Delete failed for Ovning ID {}: ", id, e);
            redirectAttributes.addFlashAttribute("errorMessage", "ovning.error.delete");
        } catch (Exception e) {
            logger.error("Unexpected error in delete for Ovning ID {}: ", id, e);
            redirectAttributes.addFlashAttribute("errorMessage", "ovning.error.unexpected");
        }
        return "redirect:/" + VIEW_FOLDER;
    }

    /**
     * Displays the details of an existing Ovning.
     *
     * @param id    The ID of the Ovning to view.
     * @param model The model to pass attributes to the view.
     * @return The name of the view template (ovningar/view) or error template (ovningar/error) if not found.
     */
    @GetMapping({"/{id}", "/{id}/"})
    public String view(@PathVariable Long id, Model model) {
        try {
            Optional<Ovning> ovningOpt = ovningService.findById(id);
            if (ovningOpt.isPresent()) {
                model.addAttribute(ENTITY_ATTR_NAME, ovningOpt.get());
                return VIEW_FOLDER + "/view";
            } else {
                logger.warn("Ovning not found for ID {}", id);
                model.addAttribute("errorMessage", "ovning.error.notfound");
                model.addAttribute("entityId", id);
                return VIEW_FOLDER + "/error";
            }
        } catch (Exception e) {
            logger.error("Unexpected error while viewing Ovning ID {}: ", id, e);
            model.addAttribute("errorMessage", "ovning.error.unexpected");
            model.addAttribute("entityId", id);
            return VIEW_FOLDER + "/error";
        }
    }
}