package br.com.agcapital.taskmanager.service;

import br.com.agcapital.taskmanager.dto.ProjectRequestDTO;
import br.com.agcapital.taskmanager.dto.ProjectResponseDTO;
import br.com.agcapital.taskmanager.entity.Customer;
import br.com.agcapital.taskmanager.entity.Project;
import br.com.agcapital.taskmanager.entity.Task;
import br.com.agcapital.taskmanager.repository.ProjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class ProjectServiceTest {

    @Autowired
    private ProjectService projectService;

    @MockBean
    private ProjectRepository projectRepository;

    private Customer customer;

    @BeforeEach
    void setUp() {
        customer = new Customer();
        customer.setId(1L);
        customer.setCustomerName("Customer 1");
    }

    @Test
    void saveProject_newProject_shouldSaveProject() {
        ProjectRequestDTO dto = new ProjectRequestDTO();
        dto.setProjectName("Project 1");

        Project project = new Project();
        project.setId(1L);
        project.setProjectName("Project 1");
        project.setCustomer(customer);

        when(projectRepository.save(any(Project.class))).thenReturn(project);

        Project savedProject = projectService.saveProject(dto, customer);

        assertNotNull(savedProject);
        assertEquals("Project 1", savedProject.getProjectName());
        assertEquals(customer, savedProject.getCustomer());

        verify(projectRepository, times(1)).save(any(Project.class));
    }

    @Test
    void saveProject_existingProject_shouldUpdateProject() {
        ProjectRequestDTO dto = new ProjectRequestDTO();
        dto.setId(1L);
        dto.setProjectName("Updated Project 1");

        Project existingProject = new Project();
        existingProject.setId(1L);
        existingProject.setProjectName("Project 1");
        existingProject.setCustomer(customer);

        when(projectRepository.findById(1L)).thenReturn(Optional.of(existingProject));
        when(projectRepository.save(any(Project.class))).thenReturn(existingProject);

        Project updatedProject = projectService.saveProject(dto, customer);

        assertNotNull(updatedProject);
        assertEquals("Updated Project 1", updatedProject.getProjectName());
        assertEquals(customer, updatedProject.getCustomer());

        verify(projectRepository, times(1)).findById(1L);
        verify(projectRepository, times(1)).save(any(Project.class));
    }

    @Test
    void findById_existingId_shouldReturnProject() {
        Project project = new Project();
        project.setId(1L);
        project.setProjectName("Project 1");

        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));

        Project foundProject = projectService.findById(1L);

        assertNotNull(foundProject);
        assertEquals(1L, foundProject.getId());
        assertEquals("Project 1", foundProject.getProjectName());

        verify(projectRepository, times(1)).findById(1L);
    }

    @Test
    void findById_nonExistingId_shouldReturnNull() {
        when(projectRepository.findById(1L)).thenReturn(Optional.empty());

        Project foundProject = projectService.findById(1L);

        assertNull(foundProject);

        verify(projectRepository, times(1)).findById(1L);
    }

    @Test
    void listProjects_shouldReturnAllProjects() {
        Project project1 = new Project();
        project1.setId(1L);
        project1.setProjectName("Project 1");
        project1.setCustomer(customer);

        Task task1 = new Task();
        task1.setId(1L);
        task1.setTaskName("Task 1");
        task1.setProject(project1);

        Project project2 = new Project();
        project2.setId(2L);
        project2.setProjectName("Project 2");
        project2.setCustomer(customer);

        Task task2 = new Task();
        task2.setId(2L);
        task2.setTaskName("Task 2");
        task2.setProject(project2);

        project1.setTasks(List.of(task1));
        project2.setTasks(List.of(task2));

        List<Project> projects = List.of(project1, project2);

        when(projectRepository.findAll()).thenReturn(projects);

        List<ProjectResponseDTO> projectResponseDTOList = projectService.listProjects();

        assertNotNull(projectResponseDTOList);
        assertEquals(2, projectResponseDTOList.size());

        verify(projectRepository, times(1)).findAll();
    }

    @Test
    void deleteProject_existingId_shouldDeleteProject() {
        Long projectId = 1L;

        doNothing().when(projectRepository).deleteById(projectId);

        projectService.deleteProject(projectId);

        verify(projectRepository, times(1)).deleteById(projectId);
    }
}
