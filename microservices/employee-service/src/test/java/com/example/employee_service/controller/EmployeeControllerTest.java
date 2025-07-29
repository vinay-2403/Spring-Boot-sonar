package com.example.employee_service.controller;

import com.example.employee_service.dto.EmployeeRequestDTO;
import com.example.employee_service.dto.EmployeeResponseDTO;
import com.example.employee_service.dto.ProjectDTO;
import com.example.employee_service.service.EmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EmployeeController.class)
@Import(EmployeeControllerTest.MockServiceConfig.class)
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EmployeeService service;

    @Autowired
    private ObjectMapper objectMapper;

    @TestConfiguration
    static class MockServiceConfig {
        @Bean
        public EmployeeService employeeService() {
            return mock(EmployeeService.class);
        }
    }

    @Test
    void testGetAllEmployees() throws Exception {
        EmployeeResponseDTO emp = new EmployeeResponseDTO(1L, "John", "HR", "P001", null);
        when(service.getAllEmployees()).thenReturn(List.of(emp));

        mockMvc.perform(get("/employee"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("John"));
    }

    @Test
    void testGetById() throws Exception {
        ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setId(100L);
        projectDTO.setTitle("ProjectX");
        projectDTO.setProjectCode("PX001");

        EmployeeResponseDTO response = new EmployeeResponseDTO(1L, "Jane", "IT", "PX001", projectDTO);
        when(service.getById(1L)).thenReturn(response);

        mockMvc.perform(get("/employee/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.project.title").value("ProjectX"));
    }

    @Test
    void testSaveEmployee() throws Exception {
        EmployeeRequestDTO request = new EmployeeRequestDTO("Tom", "Finance", "tom@example.com", "PRJ007", "CA");
        EmployeeResponseDTO response = new EmployeeResponseDTO(1L, "Tom", "Finance", "PRJ007", null);

        when(service.save(any())).thenReturn(response);

        mockMvc.perform(post("/employee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Tom"));
    }

    @Test
    void testUpdateEmployee() throws Exception {
        EmployeeRequestDTO request = new EmployeeRequestDTO("Updated", "Dept", "update@mail.com", "CODE", "Addr");
        EmployeeResponseDTO response = new EmployeeResponseDTO(1L, "Updated", "Dept", "CODE", null);

        when(service.updateEmployee(eq(1L), any())).thenReturn(response);

        mockMvc.perform(put("/employee/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated"));
    }

    @Test
    void testDeleteEmployee() throws Exception {
        doNothing().when(service).deleteEmployee(1L);

        mockMvc.perform(delete("/employee/1"))
                .andExpect(status().isOk());
    }
}
