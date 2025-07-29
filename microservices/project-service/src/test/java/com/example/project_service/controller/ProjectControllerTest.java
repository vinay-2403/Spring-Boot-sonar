package com.example.project_service.controller;

import com.example.project_service.dto.ProjectRequestDTO;
import com.example.project_service.dto.ProjectResponseDTO;
import com.example.project_service.service.ProjectService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProjectController.class)
@Import(ProjectControllerTest.MockConfig.class)
class ProjectControllerTest {

    @TestConfiguration
    static class MockConfig {
        @Bean
        public ProjectService projectService() {
            return Mockito.mock(ProjectService.class);
        }
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetAllProjects() throws Exception {
        ProjectResponseDTO dto = new ProjectResponseDTO(1L, "Alpha", "PRJ001");
        when(projectService.getAllProjects()).thenReturn(List.of(dto));

        mockMvc.perform(get("/projects"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Alpha"));
    }

    @Test
    void testCreateProject() throws Exception {
        ProjectRequestDTO request = new ProjectRequestDTO("Beta", "Beta Desc", "PRJ002");
        ProjectResponseDTO response = new ProjectResponseDTO(2L, "Beta", "PRJ002");

        when(projectService.saveProject(any())).thenReturn(response);

        mockMvc.perform(post("/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Beta"));
    }

    @Test
    void testGetProjectById() throws Exception {
        ProjectResponseDTO response = new ProjectResponseDTO(1L, "Gamma", "PRJ003");
        when(projectService.getProjectById(1L)).thenReturn(response);

        mockMvc.perform(get("/projects/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.projectCode").value("PRJ003"));
    }

    @Test
    void testDeleteProject() throws Exception {
        mockMvc.perform(delete("/projects/1"))
                .andExpect(status().isOk());
    }
}
