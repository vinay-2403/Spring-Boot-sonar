package com.example.employee_service.service;

import com.example.employee_service.dto.EmployeeRequestDTO;
import com.example.employee_service.dto.EmployeeResponseDTO;
import com.example.employee_service.dto.ProjectDTO;
import com.example.employee_service.entity.Employee;
import com.example.employee_service.exception.EmployeeNotFoundException;
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
    void setUp() {
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

        EmployeeResponseDTO response = service.save(dto);

        assertEquals("John", response.getName());
        assertEquals("PROJ001", response.getEmployeeAssignedProjectCode());
    }

    @Test
    void testGetByIdWithProject() {
        Employee employee = new Employee(1L, "Jane", "IT", "jane@example.com", "PROJ002", "LA");
        ProjectDTO projectDTO = new ProjectDTO(100L, "Alpha", "PROJ002");

        when(repository.findById(1L)).thenReturn(Optional.of(employee));
        when(projectClient.getProjectByCode("PROJ002")).thenReturn(projectDTO);

        EmployeeResponseDTO response = service.getById(1L);

        assertEquals("Jane", response.getName());
        assertNotNull(response.getProject());
        assertEquals("Alpha", response.getProject().getTitle());
    }


    @Test
    void testGetAllEmployees() {
        Employee emp = new Employee(1L, "Smith", "Dev", "smith@mail.com", "PROJ003", "NY");
        when(repository.findAll()).thenReturn(List.of(emp));

        List<EmployeeResponseDTO> result = service.getAllEmployees();

        assertEquals(1, result.size());
        assertEquals("Smith", result.get(0).getName());
    }

    @Test
    void testUpdateEmployee() {
        Employee existing = new Employee(1L, "Old", "Dept", "old@mail.com", "CODE1", "Loc");
        EmployeeRequestDTO dto = new EmployeeRequestDTO("New", "NewDept", "new@mail.com", "CODE2", "NewLoc");

        when(repository.findById(1L)).thenReturn(Optional.of(existing));
        when(repository.save(any(Employee.class))).thenAnswer(inv -> inv.getArgument(0));

        EmployeeResponseDTO response = service.updateEmployee(1L, dto);

        assertEquals("New", response.getName());
        assertEquals("NewDept", response.getDepartment());
        assertEquals("CODE2", response.getEmployeeAssignedProjectCode());
    }

    @Test
    void testDeleteEmployee() {
        doNothing().when(repository).deleteById(1L);
        service.deleteEmployee(1L);
        verify(repository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteEmployeeThrowsException() {
        doThrow(new RuntimeException("Employee not found with id: 99")).when(repository).deleteById(99L);

        Exception exception = assertThrows(RuntimeException.class, () -> service.deleteEmployee(99L));
        assertEquals("Employee not found with id: 99", exception.getMessage());
    }

    @Test
    void testGetByIdThrowsEmployeeNotFoundException() {
        when(repository.findById(999L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EmployeeNotFoundException.class, () -> service.getById(999L));

        assertEquals("employee not found with id999", exception.getMessage());
    }



    @Test
    void testPatchEmployee_EmployeeNotFound() {
        EmployeeRequestDTO patchDto = new EmployeeRequestDTO(null, "Ops", null, null, null);

        when(repository.findById(404L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EmployeeNotFoundException.class, () -> service.patchEmployee(404L, patchDto));

        assertEquals("employee not found with id404", exception.getMessage());
    }

    @Test
    void testUpdateEmployee_EmployeeNotFound() {
        EmployeeRequestDTO updateDto = new EmployeeRequestDTO("Mike", "Finance", "mike@mail.com", "PRJ05", "FL");

        when(repository.findById(123L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EmployeeNotFoundException.class, () -> service.updateEmployee(123L, updateDto));

        assertEquals("employee not found with id123", exception.getMessage());
    }


}
