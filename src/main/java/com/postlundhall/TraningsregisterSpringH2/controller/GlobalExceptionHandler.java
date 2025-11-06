package com.postlundhall.TraningsregisterSpringH2.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import org.thymeleaf.exceptions.TemplateInputException;
import org.thymeleaf.exceptions.TemplateProcessingException;

/**
 * Configuration class for handling exceptions and directing to exception thymeleaf pages.
 * Includes 403 Access denied, 404 Not found,  500 internal server error, and other general exceptions.
 * @author postlundhall
 * @since 1.0
 */
@ControllerAdvice
public class GlobalExceptionHandler {
    // Initialize a logger for logging exceptions
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Resolves a Thymeleaf template name for Thymeleaf-related exceptions:<br>
     * - TemplateProcessingException (tpe)<br>
     * - TemplateInputException (tie)
     *
     * @param e the exception from which the template name is derived.
     * @return template name, or null if exception type is not tpe or tie.
     */
    private String resolveTemplateName(Exception e) {
        if (e instanceof TemplateProcessingException tpe) {
            return tpe.getTemplateName();
        }
        if (e instanceof TemplateInputException tie) {
            return tie.getTemplateName();
        }
        return null;
    }

    /**
     * Handles {@code AccessDeniedException} where visitor is unauthorized to access the specified endpoint.
     * Logs exception message for debugging purposes and adds an i18n key for a 403 error message to the MVC model,
     * which is displayed via {@code error/403} Thymeleaf template.
     * @param e the exception that is handled.
     * @param model MVC model that passes error message to Thymeleaf template.
     * @return name of the 403 error template.
     */
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public String handleAccessDeniedException(AccessDeniedException e, Model model) {
        logger.warn("Access denied: {}", e.getMessage());
        model.addAttribute("errorMessage", "error.403.message");
        return "error/403";
    }

    /**
     * Handles NotFound-exceptions ({@code NoHandlerFoundException} or {@code NoResourceFoundException})
     * where the endpoint is not handled by the application or the resource cannot be found.
     * Logs exception name and message for debugging purposes.
     * Adds an i18n key for a 404 error message to the MVC model,
     * which is displayed via {@code error/404} Thymeleaf template.
     * @param e the exception that is handled.
     * @param model MVC model that passes error message to Thymeleaf template.
     * @return name of the 404 error template.
     */
    @ExceptionHandler({NoHandlerFoundException.class, NoResourceFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleNotFoundException(Exception e, Model model) {
        logger.warn("Page not found: {}: {}", e.getClass().getSimpleName(), e.getMessage());
        model.addAttribute("errorMessage", "error.404.message");
        return "error/404";
    }

    /**
     * Handles Thymeleaf-related exceptions ({@code TemplateProcessingException} or {@code TemplateInputException})
     * that cause internal server error when processing a request.
     * Logs template name and the exception for debugging purposes.
     * Adds an i18n key for a 500 error message to the MVC model,
     * which is displayed via {@code error/500} Thymeleaf template.
     * @param e the exception that is handled.
     * @param model MVC model that passes error message to Thymeleaf template.
     * @return name of the 500 error template.
     */
    @ExceptionHandler({TemplateProcessingException.class, TemplateInputException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleThymeleafException(Exception e, Model model) {
        String templateName = resolveTemplateName(e);
        logger.error("Template processing error: template={}", templateName, e);
        model.addAttribute("errorMessage", "error.500.message");
        return "error/500";
    }

    /**
     * Handles uncaught exceptions that cause internal server error when processing a request.
     * Logs the exception for debugging purposes and adds an i18n key for a generic error message to the MVC model,
     * which is displayed via {@code error/general} Thymeleaf template.
     * @param e the exception that is handled.
     * @param model MVC model that passes error message to Thymeleaf template.
     * @return name of the generic error template.
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleException(Exception e, Model model) {
        logger.error("Unhandled exception:", e);
        model.addAttribute("errorMessage", "error.general.message");
        return "error/general";
    }
}
