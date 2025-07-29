package com.example.project_service.controller;


import com.example.project_service.dto.ProjectRequestDTO;
import com.example.project_service.dto.ProjectResponseDTO;
import com.example.project_service.service.ProjectService;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/projects")
public class ProjectController {

    private final ProjectService service;

    public ProjectController(ProjectService service) {
        this.service = service;
    }

    @PostMapping
    public ProjectResponseDTO createProject(@RequestBody ProjectRequestDTO dto) {
        return service.saveProject(dto);
    }

    @GetMapping("/{id}")
    public ProjectResponseDTO getProject(@PathVariable Long id) {
        return service.getProjectById(id);
    }

    @GetMapping
    public List<ProjectResponseDTO> getAllProjects() {
        return service.getAllProjects();
    }

    @PutMapping("/{id}")
    public ProjectResponseDTO updateProject(
            @PathVariable Long id,
            @RequestBody ProjectRequestDTO dto) {
        return service.updateProject(id, dto);
    }

    @PatchMapping("/{id}")
    public ProjectResponseDTO patchProject(
            @PathVariable Long id,
            @RequestBody ProjectRequestDTO dto) {
        return service.patchProject(id, dto);
    }

    @DeleteMapping("/{id}")
    public void deleteProject(@PathVariable Long id) {
        service.deleteProject(id);
    }

    @GetMapping("/code/{code}")
    public ProjectResponseDTO getProjectByCode(@PathVariable String code) {
        return service.getProjectByCode(code);
    }

}
