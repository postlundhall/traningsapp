package com.postlundhall.TraningsregisterSpringH2.service;

import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility class for sanitizing user input to prevent XSS attacks and invalid data.
 * Uses <a href="https://jsoup.org/">Jsoup</a> with a strict {@link Safelist#none()} policy
 * to strip all HTML tags, and enforces:
 * <ul>
 *   <li>Maximum length restriction</li>
 *   <li>Removal of control characters (matched by {@code \p{Cntrl}})</li>
 *   <li>Logging of violations</li>
 * </ul>
 * On validation failure, throws {@link ValidationException} with reason {@code "length"} or {@code "characters"}.
 *
 * @see ValidationException
 * @see Safelist
 *
 * @author postlundhall
 * @since 1.0
 */
public class SanitizationUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(SanitizationUtil.class);

    // Safelist allowing only plain text - no HTML tags.
    private static final Safelist SAFELIST = Safelist.none();

    // Regex pattern to match any Unicode control characters
    private static final String INVALID_CHAR_PATTERN = "[\\p{Cntrl}]";

    /**
     * Sanitizes the input string by:
     * <ol>
     *   <li>Trimming whitespace</li>
     *   <li>Stripping all HTML using Jsoup</li>
     *   <li>Enforcing maximum length</li>
     *   <li>Rejecting control characters</li>
     * </ol>
     *
     * @param input     the raw input string to sanitize
     * @param maxLength the maximum allowed length after sanitization
     * @return the sanitized string, or {@code null} if input is {@code null}, or becomes empty
     * @throws ValidationException if length exceeds {@code maxLength} or invalid characters are found
     */
    public static String sanitize(String input, int maxLength) {
        if (input == null) {
            return null;
        }
        String cleaned = Jsoup.clean(input.trim(), SAFELIST);
        if (cleaned.isEmpty()) {
            return null;
        }
        if (cleaned.length() > maxLength) {
            LOGGER.warn("Input exceeds max length of {}: {}", maxLength,
                    cleaned.substring(0, Math.min(cleaned.length(), 50)));
            throw new ValidationException("length");
        }
        if (cleaned.matches(".*" + INVALID_CHAR_PATTERN + ".*")) {
            LOGGER.warn("Input contains invalid characters: {}", cleaned.substring(0, Math.min(cleaned.length(), 50)));
            throw new ValidationException("characters");
        }
        return cleaned;
    }

    /**
     * Sanitizes the input string with a default maximum length of 255 characters.
     *
     * @param input the raw input string to sanitize
     * @return the sanitized string, or {@code null} if input is {@code null}, or becomes empty
     * @throws ValidationException if validation fails
     * @see #sanitize(String, int)
     */
    public static String sanitize(String input) {
        return sanitize(input, 255);
    }
}