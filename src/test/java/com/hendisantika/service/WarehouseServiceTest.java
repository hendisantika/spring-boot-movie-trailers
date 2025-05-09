package com.hendisantika.service;

import com.hendisantika.exception.FileNotFoundException;
import com.hendisantika.exception.WarehouseException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

class WarehouseServiceTest {

    @TempDir
    Path tempDir;
    private WarehouseService warehouseService;

    @BeforeEach
    void setUp() {
        warehouseService = new WarehouseService();
        // Set the storage location to our temporary directory
        try {
            java.lang.reflect.Field field = WarehouseService.class.getDeclaredField("storageLocation");
            field.setAccessible(true);
            field.set(warehouseService, tempDir.toString());
        } catch (Exception e) {
            fail("Failed to set up test: " + e.getMessage());
        }

        // Initialize the storage
        warehouseService.startStoreFiles();
    }

    @AfterEach
    void tearDown() {
        // Clean up any files created during tests
        try {
            Files.list(tempDir).forEach(file -> {
                try {
                    Files.deleteIfExists(file);
                } catch (IOException e) {
                    // Ignore
                }
            });
        } catch (IOException e) {
            // Ignore
        }
    }

    @Test
    void startStoreFiles_shouldCreateDirectory() {
        // The directory should have been created in setUp
        assertTrue(Files.exists(tempDir), "Storage directory should exist");
        assertTrue(Files.isDirectory(tempDir), "Storage location should be a directory");
    }

    @Test
    void storeFile_shouldStoreFileAndReturnFilename() {
        // Given
        String filename = "test-file.txt";
        String content = "Test content";
        MultipartFile file = new MockMultipartFile(filename, filename, "text/plain", content.getBytes());

        // When
        String storedFilename = warehouseService.storeFile(file);

        // Then
        assertEquals(filename, storedFilename, "Should return the original filename");
        assertTrue(Files.exists(tempDir.resolve(filename)), "File should exist in storage location");

        // Verify content
        try {
            String storedContent = Files.readString(tempDir.resolve(filename));
            assertEquals(content, storedContent, "File content should match");
        } catch (IOException e) {
            fail("Failed to read stored file: " + e.getMessage());
        }
    }

    @Test
    void storeFile_shouldThrowExceptionForEmptyFile() {
        // Given
        MultipartFile emptyFile = new MockMultipartFile("empty.txt", "empty.txt", "text/plain", new byte[0]);

        // When/Then
        WarehouseException exception = assertThrows(WarehouseException.class, () -> {
            warehouseService.storeFile(emptyFile);
        });

        assertEquals("Can't store an empty file", exception.getMessage());
    }

    @Test
    void fileUpload_shouldReturnCorrectPath() {
        // Given
        String filename = "test-file.txt";

        // When
        Path path = warehouseService.fileUpload(filename);

        // Then
        assertEquals(tempDir.resolve(filename), path, "Should return the correct path");
    }

    @Test
    void uploadAsResource_shouldReturnResourceForExistingFile() throws IOException {
        // Given
        String filename = "test-file.txt";
        String content = "Test content";
        Path filePath = tempDir.resolve(filename);
        Files.write(filePath, content.getBytes());

        // When
        Resource resource = warehouseService.uploadAsResource(filename);

        // Then
        assertTrue(resource.exists(), "Resource should exist");
        assertEquals(content, new String(resource.getInputStream().readAllBytes()), "Resource content should match");
    }

    @Test
    void uploadAsResource_shouldThrowExceptionForNonExistentFile() {
        // Given
        String nonExistentFile = "non-existent.txt";

        // When/Then
        assertThrows(FileNotFoundException.class, () -> {
            warehouseService.uploadAsResource(nonExistentFile);
        });
    }

    @Test
    void deleteArchive_shouldDeleteExistingFile() throws IOException {
        // Given
        String filename = "test-file.txt";
        Path filePath = tempDir.resolve(filename);
        Files.write(filePath, "Test content".getBytes());
        assertTrue(Files.exists(filePath), "File should exist before deletion");

        // When
        warehouseService.deleteArchive(filename);

        // Then
        assertFalse(Files.exists(filePath), "File should be deleted");
    }

    @Test
    void deleteArchive_shouldNotThrowExceptionForNonExistentFile() {
        // Given
        String nonExistentFile = "non-existent.txt";

        // When/Then
        assertDoesNotThrow(() -> {
            warehouseService.deleteArchive(nonExistentFile);
        });
    }
}