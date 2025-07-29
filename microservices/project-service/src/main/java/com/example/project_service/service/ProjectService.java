package com.example.project_service.service;



import com.example.project_service.dto.ProjectRequestDTO;
import com.example.project_service.dto.ProjectResponseDTO;

import java.util.List;

public interface ProjectService {

    ProjectResponseDTO saveProject(ProjectRequestDTO dto);
    ProjectResponseDTO getProjectById(Long id);
    List<ProjectResponseDTO> getAllProjects();
    ProjectResponseDTO updateProject(Long id, ProjectRequestDTO dto);
    ProjectResponseDTO patchProject(Long id, ProjectRequestDTO dto);
    void deleteProject(Long id);
    ProjectResponseDTO getProjectByCode(String code);
}

