package com.example.employee_service.service;

import com.example.employee_service.dto.EmployeeRequestDTO;
import com.example.employee_service.dto.EmployeeResponseDTO;

import java.util.List;

public interface EmployeeService {
    List<EmployeeResponseDTO> getAllEmployees();
    EmployeeResponseDTO getById(Long id);
    EmployeeResponseDTO save(EmployeeRequestDTO dto);
    EmployeeResponseDTO updateEmployee(Long id,EmployeeRequestDTO dto);
    EmployeeResponseDTO patchEmployee(Long id,EmployeeRequestDTO dto);
    void deleteEmployee(Long id);

}

