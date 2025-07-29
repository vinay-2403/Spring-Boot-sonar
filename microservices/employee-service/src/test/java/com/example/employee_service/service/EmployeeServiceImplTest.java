package com.example.employee_service.service;

import com.example.employee_service.dto.EmployeeRequestDTO;
import com.example.employee_service.dto.ProjectDTO;
import com.example.employee_service.entity.Employee;
import com.example.employee_service.feign.ProjectClient;
import com.example.employee_service.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EmployeeServiceImplTest {

    private EmployeeRepository repository;
    private ModelMapper mapper;
    private ProjectClient projectClient;
    private EmployeeServiceImpl service;

    @BeforeEach
    void setup() {
        repository = mock(EmployeeRepository.class);
        mapper = new ModelMapper();
        projectClient = mock(ProjectClient.class);
        service = new EmployeeServiceImpl(repository, mapper, projectClient);
    }

    @Test
    void testSaveEmployee() {
        EmployeeRequestDTO dto = new EmployeeRequestDTO("John", "HR", "john@example.com", "PROJ001", "NY");
        Employee savedEmployee = mapper.map(dto, Employee.class);
        savedEmployee.setId(1L);

        when(repository.save(any(Employee.class))).thenReturn(savedEmployee);

        var response = service.save(dto);

        assertEquals("John", response.getName());
        assertEquals("PROJ001", response.getEmployeeAssignedProjectCode());
    }

    @Test
    void testGetById_WithProject() {
        Employee employee = new Employee(1L, "Jane", "IT", "jane@example.com", "PROJ002", "LA");
        ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setId(100L);
        projectDTO.setTitle("Alpha");
        projectDTO.setProjectCode("PROJ002");

        when(repository.findById(1L)).thenReturn(Optional.of(employee));
        when(projectClient.getProjectByCode("PROJ002")).thenReturn(projectDTO);

        var result = service.getById(1L);

        assertEquals("Jane", result.getName());
        assertNotNull(result.getProject());
        assertEquals("Alpha", result.getProject().getTitle());
    }

    @Test
    void testDeleteEmployee() {
        doNothing().when(repository).deleteById(1L);
        service.deleteEmployee(1L);
        verify(repository, times(1)).deleteById(1L);
    }
}
