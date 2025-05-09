package com.hendisantika.controller;

import com.hendisantika.service.WarehouseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class AssetsControllerTest {

    @Mock
    private WarehouseService warehouseService;

    @InjectMocks
    private AssetsController assetsController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(assetsController).build();
    }

    @Test
    void getResource_shouldReturnResource() throws Exception {
        // Given
        String filename = "test-image.jpg";
        byte[] fileContent = "test image content".getBytes();
        Resource resource = new ByteArrayResource(fileContent) {
            @Override
            public String getFilename() {
                return filename;
            }
        };

        when(warehouseService.uploadAsResource(eq(filename))).thenReturn(resource);

        // When/Then
        mockMvc.perform(get("/assets/{filename}", filename))
                .andExpect(status().isOk())
                .andExpect(content().bytes(fileContent));
    }
}