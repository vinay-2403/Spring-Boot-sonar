package com.example.project_service.service;

import com.example.project_service.dto.ProjectRequestDTO;
import com.example.project_service.dto.ProjectResponseDTO;
import com.example.project_service.entity.Project;
import com.example.project_service.repository.ProjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.mockito.Mock;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProjectServiceImplTest {

    @Mock
    private ProjectRepository repository;

    private ProjectServiceImpl service;

    private final ModelMapper mapper = new ModelMapper();

    @BeforeEach
    void setup() {
        service = new ProjectServiceImpl(repository, mapper);
    }

    @Test
    void testSaveProject() {
        ProjectRequestDTO dto = new ProjectRequestDTO("Title", "Desc", "CODE123");
        Project savedEntity = new Project(1L, "Title", "Desc", "CODE123");

        when(repository.save(any())).thenReturn(savedEntity);

        ProjectResponseDTO result = service.saveProject(dto);

        assertEquals("Title", result.getTitle());
        verify(repository, times(1)).save(any());
    }

    @Test
    void testGetProjectById() {
        Project project = new Project(2L, "Test", "Desc", "P02");
        when(repository.findById(2L)).thenReturn(Optional.of(project));

        ProjectResponseDTO result = service.getProjectById(2L);

        assertEquals("P02", result.getProjectCode());
    }

    @Test
    void testDeleteProject() {
        service.deleteProject(3L);
        verify(repository, times(1)).deleteById(3L);
    }
}
