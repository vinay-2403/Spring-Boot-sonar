package com.example.project_service.service;

import com.example.project_service.dto.ProjectRequestDTO;
import com.example.project_service.dto.ProjectResponseDTO;
import com.example.project_service.entity.Project;
import com.example.project_service.exception.ProjectCodeNotFoundException;
import com.example.project_service.exception.ProjectNotFoundException;
import com.example.project_service.repository.ProjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.mockito.Mock;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
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

    @Test
    void testGetAllProjects() {
        Project project1 = new Project(1L, "Proj1", "Desc1", "C001");
        Project project2 = new Project(2L, "Proj2", "Desc2", "C002");

        when(repository.findAll()).thenReturn(List.of(project1, project2));

        var results = service.getAllProjects();

        assertEquals(2, results.size());
        assertEquals("Proj1", results.get(0).getTitle());
        assertEquals("C002", results.get(1).getProjectCode());

        verify(repository, times(1)).findAll();
    }

    @Test
    void testUpdateProject() {
        Long id = 1L;
        Project existing = new Project(id, "Old", "Old Desc", "OLD123");
        ProjectRequestDTO dto = new ProjectRequestDTO("New", "New Desc", "NEW123");

        when(repository.findById(id)).thenReturn(Optional.of(existing));
        when(repository.save(any())).thenReturn(existing);

        ProjectResponseDTO result = service.updateProject(id, dto);

        assertEquals("New", result.getTitle());
        assertEquals("NEW123", result.getProjectCode());
        verify(repository).findById(id);
        verify(repository).save(existing);
    }

    @Test
    void testGetProjectByCode() {
        Project project = new Project(4L, "CodeProj", "Desc", "CODE100");
        when(repository.findByProjectCode("CODE100")).thenReturn(Optional.of(project));

        ProjectResponseDTO result = service.getProjectByCode("CODE100");

        assertEquals("CodeProj", result.getTitle());
        assertEquals("CODE100", result.getProjectCode());
    }

    @Test
    void testGetProjectById_NotFound() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ProjectNotFoundException.class, () ->
                service.getProjectById(99L));

        assertTrue(exception.getMessage().contains("Project not found with id99"));
    }

    @Test
    void testGetProjectByCode_NotFound() {
        when(repository.findByProjectCode("INVALID")).thenReturn(Optional.empty());

        Exception exception = assertThrows(ProjectCodeNotFoundException.class, () ->
                service.getProjectByCode("INVALID"));

        assertTrue(exception.getMessage().contains("Project not found with codeINVALID"));
    }

    @Test
    void testPatchProject() {
        Long id = 10L;
        Project existing = new Project(id, "Old Title", "Old Desc", "OLD100");
        ProjectRequestDTO patchDto = new ProjectRequestDTO("New Title", null, "NEW100");

        when(repository.findById(id)).thenReturn(Optional.of(existing));
        when(repository.save(any())).thenReturn(existing);

        ProjectResponseDTO result = service.patchProject(id, patchDto);

        assertEquals("New Title", result.getTitle());
        assertEquals("NEW100", result.getProjectCode());
        verify(repository).findById(id);
        verify(repository).save(existing);
    }

    @Test
    void testPatchProject_TitleOnly() {
        Long id = 101L;
        Project existing = new Project(id, "Old", "Old Desc", "OLD1");
        ProjectRequestDTO patchDto = new ProjectRequestDTO("New", null, null);

        when(repository.findById(id)).thenReturn(Optional.of(existing));
        when(repository.save(any())).thenReturn(existing);

        ProjectResponseDTO result = service.patchProject(id, patchDto);

        assertEquals("New", result.getTitle());
        assertEquals("OLD1", result.getProjectCode());
    }

    @Test
    void testPatchProject_DescriptionOnly() {
        Long id = 102L;
        Project existing = new Project(id, "Old", "Old Desc", "OLD2");
        ProjectRequestDTO patchDto = new ProjectRequestDTO(null, "Updated Desc", null);

        when(repository.findById(id)).thenReturn(Optional.of(existing));
        when(repository.save(any())).thenReturn(existing);

        ProjectResponseDTO result = service.patchProject(id, patchDto);

        assertEquals("Old", result.getTitle());
        assertEquals("OLD2", result.getProjectCode());
    }

    @Test
    void testPatchProject_CodeOnly() {
        Long id = 103L;
        Project existing = new Project(id, "Old", "Old Desc", "OLD3");
        ProjectRequestDTO patchDto = new ProjectRequestDTO(null, null, "NEW3");

        when(repository.findById(id)).thenReturn(Optional.of(existing));
        when(repository.save(any())).thenReturn(existing);

        ProjectResponseDTO result = service.patchProject(id, patchDto);

        assertEquals("Old", result.getTitle());
        assertEquals("NEW3", result.getProjectCode());
    }

    @Test
    void testPatchProject_AllFields() {
        Long id = 104L;
        Project existing = new Project(id, "Old", "Old Desc", "OLD4");
        ProjectRequestDTO patchDto = new ProjectRequestDTO("New", "New Desc", "NEW4");

        when(repository.findById(id)).thenReturn(Optional.of(existing));
        when(repository.save(any())).thenReturn(existing);

        ProjectResponseDTO result = service.patchProject(id, patchDto);

        assertEquals("New", result.getTitle());
        assertEquals("NEW4", result.getProjectCode());
    }

    @Test
    void testPatchProject_NoFields() {
        Long id = 105L;
        Project existing = new Project(id, "Old", "Old Desc", "OLD5");
        ProjectRequestDTO patchDto = new ProjectRequestDTO(null, null, null);

        when(repository.findById(id)).thenReturn(Optional.of(existing));
        when(repository.save(any())).thenReturn(existing);

        ProjectResponseDTO result = service.patchProject(id, patchDto);

        assertEquals("Old", result.getTitle());
        assertEquals("OLD5", result.getProjectCode());
    }

}
