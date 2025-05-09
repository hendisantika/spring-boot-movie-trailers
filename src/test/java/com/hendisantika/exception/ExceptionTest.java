package com.hendisantika.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ExceptionTest {

    @Test
    void fileNotFoundException_shouldHaveCorrectAnnotation() {
        // Given
        Class<FileNotFoundException> clazz = FileNotFoundException.class;

        // When
        ResponseStatus annotation = clazz.getAnnotation(ResponseStatus.class);

        // Then
        assertNotNull(annotation, "FileNotFoundException should have @ResponseStatus annotation");
        assertEquals(HttpStatus.NOT_FOUND, annotation.code(), "FileNotFoundException should have NOT_FOUND status code");
    }

    @Test
    void fileNotFoundException_shouldHaveCorrectMessage() {
        // Given
        String message = "Test file not found";

        // When
        FileNotFoundException exception = new FileNotFoundException(message);

        // Then
        assertEquals(message, exception.getMessage(), "Exception message should match");
    }

    @Test
    void fileNotFoundException_shouldHaveCorrectMessageAndCause() {
        // Given
        String message = "Test file not found";
        Throwable cause = new RuntimeException("Original cause");

        // When
        FileNotFoundException exception = new FileNotFoundException(message, cause);

        // Then
        assertEquals(message, exception.getMessage(), "Exception message should match");
        assertEquals(cause, exception.getCause(), "Exception cause should match");
    }

    @Test
    void warehouseException_shouldHaveCorrectMessage() {
        // Given
        String message = "Test warehouse exception";

        // When
        WarehouseException exception = new WarehouseException(message);

        // Then
        assertEquals(message, exception.getMessage(), "Exception message should match");
    }

    @Test
    void warehouseException_shouldHaveCorrectMessageAndCause() {
        // Given
        String message = "Test warehouse exception";
        Throwable cause = new RuntimeException("Original cause");

        // When
        WarehouseException exception = new WarehouseException(message, cause);

        // Then
        assertEquals(message, exception.getMessage(), "Exception message should match");
        assertEquals(cause, exception.getCause(), "Exception cause should match");
    }
}