package com.postlundhall.TraningsregisterSpringH2.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class SanitizationUtilTest {

    @Test
    void shouldSanitizeValidInput() {
        // Arrange
        String input = "Bänkpress";
        int maxLength = 255;

        // Act
        String result = SanitizationUtil.sanitize(input, maxLength);

        // Assert
        assertEquals("Bänkpress", result);
    }

    @Test
    void shouldReturnNullForNullInput() {
        // Arrange
        String input = null;
        int maxLength = 255;

        // Act
        String result = SanitizationUtil.sanitize(input, maxLength);

        // Assert
        assertNull(result);
    }

    @Test
    void shouldReturnNullForEmptyInputAfterTrim() {
        // Arrange
        String input = "   ";
        int maxLength = 255;

        // Act
        String result = SanitizationUtil.sanitize(input, maxLength);

        // Assert
        assertNull(result);
    }

    @Test
    void shouldThrowValidationExceptionForInputExceedingMaxLength() {
        // Arrange
        String input = "a".repeat(256); // Exceed maxLength of 255
        int maxLength = 255;

        // Act & Assert
        ValidationException exception = assertThrows(ValidationException.class, () -> {
            SanitizationUtil.sanitize(input, maxLength);
        });
        assertEquals("length", exception.getReason());
    }

    @Test
    void shouldThrowValidationExceptionForInvalidCharacters() {
        // Arrange
        String input = "Bänkpress\u0001"; // Control character U+0001
        int maxLength = 255;

        // Act
        String result = SanitizationUtil.sanitize(input, maxLength);

        // Assert
        assertEquals("Bänkpress", result, "Control characters should be removed by Jsoup");
    }

    @Test
    void shouldSanitizeWithDefaultMaxLength() {
        // Arrange
        String input = "Bänkpress";

        // Act
        String result = SanitizationUtil.sanitize(input);

        // Assert
        assertEquals("Bänkpress", result);
    }

    @Test
    void shouldThrowValidationExceptionForInputExceedingDefaultMaxLength() {
        // Arrange
        String input = "a".repeat(256); // Exceed default maxLength of 255

        // Act & Assert
        ValidationException exception = assertThrows(ValidationException.class, () -> {
            SanitizationUtil.sanitize(input);
        });
        assertEquals("length", exception.getReason());
    }

    @Test
    void shouldRemoveHtmlTags() {
        // Arrange
        String input = "<script>Bänkpress</script>";
        int maxLength = 255;

        // Act
        String result = SanitizationUtil.sanitize(input, maxLength);

        // Assert
        assertNull(result); // Safelist.none() removes all content -> resulting in null
    }

    @Test
    void shouldHandleSpecialCharactersCorrectly() {
        // Arrange
        String input = "Bänkpress & Kettelbell";
        int maxLength = 255;

        // Act
        String result = SanitizationUtil.sanitize(input, maxLength);

        // Assert
        assertEquals("Bänkpress &amp; Kettelbell", result); // Expect escaped ampersand
    }
}