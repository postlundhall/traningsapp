package com.postlundhall.TraningsregisterSpringH2.service;

/**
 * An exception thrown when input sanitization or validation fails.
 * Used by {@link SanitizationUtil} to indicate specific validation errors
 * such as excessive length or invalid characters.
 *
 * @see SanitizationUtil
 * @author postlundhall
 * @since 1.0
 */
public class ValidationException extends RuntimeException {
    private final String reason;

    /**
     * Constructs a new validation exception with the specified reason.
     *
     * @param reason the reason for the failure (e.g., "length", "characters")
     */
    public ValidationException(String reason) {
        super("Validation failed: " + reason);
        this.reason = reason;
    }

    /**
     * Returns the reason code for the validation failure.
     *
     * @return the reason string
     */
    public String getReason() {
        return reason;
    }
}