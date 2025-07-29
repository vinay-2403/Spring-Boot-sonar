package com.example.employee_service.controller;

import com.example.employee_service.dto.EmployeeRequestDTO;
import com.example.employee_service.dto.EmployeeResponseDTO;
import com.example.employee_service.service.EmployeeService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employee")
public class EmployeeController {

    private final EmployeeService service;

    public EmployeeController(EmployeeService service) {
        this.service = service;
    }

    @GetMapping
    public List<EmployeeResponseDTO> getAllEmployee() {
        return service.getAllEmployees();
    }

    @GetMapping("/{id}")
    public EmployeeResponseDTO getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @PostMapping
    public EmployeeResponseDTO save(@RequestBody EmployeeRequestDTO employee) {
        return service.save(employee);
    }

    @PutMapping("/{id}")
    public EmployeeResponseDTO updateEmployee(@PathVariable Long id,@RequestBody EmployeeRequestDTO dto){
              return service.updateEmployee(id,dto);
    }

    @PatchMapping("/{id}")
    public EmployeeResponseDTO patchEmployee(@PathVariable Long id,@RequestBody EmployeeRequestDTO dto){
        return service.patchEmployee(id,dto);
    }

    @DeleteMapping("/{id}")
    public void deleteEmployee(@PathVariable Long id){
          service.deleteEmployee(id);
    }
}

