package com.example.employee_service.service;

import com.example.employee_service.dto.EmployeeRequestDTO;
import com.example.employee_service.dto.EmployeeResponseDTO;
import com.example.employee_service.dto.ProjectDTO;
import com.example.employee_service.entity.Employee;
import com.example.employee_service.feign.ProjectClient;
import com.example.employee_service.repository.EmployeeRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository repository;

    private final ModelMapper mapper;

    private final ProjectClient projectClient;

    public EmployeeServiceImpl(EmployeeRepository repository, ModelMapper mapper, ProjectClient projectClient) {
        this.repository = repository;
        this.mapper = mapper;
        this.projectClient = projectClient;
    }

    @Override
    public List<EmployeeResponseDTO> getAllEmployees() {
        return repository.findAll().stream()
                .map(employee -> mapper.map(employee,EmployeeResponseDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public EmployeeResponseDTO getById(Long id) {
        Employee employee = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("employee not found"));

        EmployeeResponseDTO response = mapper.map(employee, EmployeeResponseDTO.class);

        if (employee.getEmployeeAssignedProjectCode() != null) {
            try {
                ProjectDTO project = projectClient.getProjectByCode(employee.getEmployeeAssignedProjectCode());
                response.setProject(project);
            } catch (Exception e) {
                System.out.println("Project service unavailable or project not found.");
            }
        }
        return response;
    }

    @Override
    public EmployeeResponseDTO save(EmployeeRequestDTO dto) {
        Employee employee=mapper.map(dto,Employee.class);
        Employee saved=repository.save(employee);
        return mapper.map(saved,EmployeeResponseDTO.class);
    }

    @Override
    public EmployeeResponseDTO updateEmployee(Long id, EmployeeRequestDTO dto) {
        Employee employee=repository.findById(id).orElseThrow(()->new RuntimeException("employee not found"));
        mapper.map(dto,employee);
        Employee saved=repository.save(employee);
        return mapper.map(saved,EmployeeResponseDTO.class);
    }

    @Override
    public EmployeeResponseDTO patchEmployee(Long id, EmployeeRequestDTO dto) {
        Employee employee = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found"));


        if (dto.getName() != null){
            employee.setName(dto.getName());
        }
        if (dto.getEmail() != null){
            employee.setEmail(dto.getEmail());
        }
        if (dto.getAddress() != null){
            employee.setAddress(dto.getAddress());
        }
        if (dto.getDepartment() != null){
            employee.setDepartment(dto.getDepartment());
        }
        if (dto.getEmployeeAssignedProjectCode() != null){
            employee.setEmployeeAssignedProjectCode(dto.getEmployeeAssignedProjectCode());
        }
        Employee updated = repository.save(employee);
        return mapper.map(updated, EmployeeResponseDTO.class);
    }

    @Override
    public void deleteEmployee(Long id) {
           repository.deleteById(id);
    }
}

