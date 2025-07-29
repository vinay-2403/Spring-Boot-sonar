package com.example.project_service.service;


import com.example.project_service.dto.ProjectRequestDTO;
import com.example.project_service.dto.ProjectResponseDTO;
import com.example.project_service.entity.Project;
import com.example.project_service.repository.ProjectRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository repository;
    private final ModelMapper mapper;

    public ProjectServiceImpl(ProjectRepository repository, ModelMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public ProjectResponseDTO saveProject(ProjectRequestDTO dto) {
        Project project = mapper.map(dto, Project.class);
        Project saved = repository.save(project);
        return mapper.map(saved, ProjectResponseDTO.class);
    }

    @Override
    public ProjectResponseDTO getProjectById(Long id) {
        Project project = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found"));
        return mapper.map(project, ProjectResponseDTO.class);
    }

    @Override
    public List<ProjectResponseDTO> getAllProjects() {
        return repository.findAll()
                .stream()
                .map(p -> mapper.map(p, ProjectResponseDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public ProjectResponseDTO updateProject(Long id, ProjectRequestDTO dto) {
        Project project = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        mapper.map(dto, project);
        Project updated = repository.save(project);
        return mapper.map(updated, ProjectResponseDTO.class);
    }

    @Override
    public ProjectResponseDTO patchProject(Long id, ProjectRequestDTO dto) {
        Project project = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        if (dto.getTitle() != null){
            project.setTitle(dto.getTitle());
        }
        if (dto.getDescription() != null){
            project.setDescription(dto.getDescription());
        }
        if (dto.getProjectCode() != null){
            project.setProjectCode(dto.getProjectCode());
        }

        Project updated = repository.save(project);
        return mapper.map(updated, ProjectResponseDTO.class);
    }

    @Override
    public void deleteProject(Long id)   {
        repository.deleteById(id);
    }

    @Override
    public ProjectResponseDTO getProjectByCode(String code) {
        Project project = repository.findByProjectCode(code)
                .orElseThrow(() -> new RuntimeException("Project with code " + code + " not found"));
        return mapper.map(project, ProjectResponseDTO.class);
    }
}


