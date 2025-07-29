package com.example.employee_service.service;

import com.example.employee_service.dto.EmployeeRequestDTO;
import com.example.employee_service.dto.EmployeeResponseDTO;
import com.example.employee_service.dto.ProjectDTO;
import com.example.employee_service.entity.Employee;
import com.example.employee_service.feign.ProjectClient;
import com.example.employee_service.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import java.util.Arrays;
import java.util.Collections;
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
    void testGetAllEmployees() {
        Employee emp = new Employee(1L, "Alice", "Admin", "alice@example.com", "PRJ123", "Delhi");
        when(repository.findAll()).thenReturn(List.of(emp));

        List<EmployeeResponseDTO> list = service.getAllEmployees();

        assertEquals(1, list.size());
        assertEquals("Alice", list.get(0).getName());
    }

    @Test
    void testGetAllEmployees_Empty() {
        when(repository.findAll()).thenReturn(Collections.emptyList());

        List<EmployeeResponseDTO> list = service.getAllEmployees();

        assertTrue(list.isEmpty());
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
    void testGetById_NotFound() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> service.getById(99L));
        assertEquals("Employee not found with id: 99", thrown.getMessage());
    }

    @Test
    void testUpdateEmployee_Success() {
        EmployeeRequestDTO request = new EmployeeRequestDTO("Updated", "Dev", "dev@mail.com", "PRJ007", "Mumbai");
        Employee existing = new Employee(1L, "Old", "IT", "old@mail.com", "PRJ001", "BLR");
        Employee updated = mapper.map(request, Employee.class);
        updated.setId(1L);

        when(repository.findById(1L)).thenReturn(Optional.of(existing));
        when(repository.save(any(Employee.class))).thenReturn(updated);

        EmployeeResponseDTO response = service.updateEmployee(1L, request);

        assertEquals("Updated", response.getName());
        assertEquals("Dev", response.getDepartment());
    }

    @Test
    void testUpdateEmployee_NotFound() {
        EmployeeRequestDTO request = new EmployeeRequestDTO("Updated", "Dev", "dev@mail.com", "PRJ007", "Mumbai");

        when(repository.findById(999L)).thenReturn(Optional.empty());

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> service.updateEmployee(999L, request));
        assertEquals("Employee not found with id: 999", thrown.getMessage());
    }

    @Test
    void testDeleteEmployee() {
        doNothing().when(repository).deleteById(1L);
        service.deleteEmployee(1L);
        verify(repository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteEmployee_NotFound() {
        doThrow(new RuntimeException("Employee not found")).when(repository).deleteById(99L);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.deleteEmployee(99L));
        assertEquals("Employee not found", ex.getMessage());
    }
}
