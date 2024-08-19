package br.com.agcapital.taskmanager.service;

import br.com.agcapital.taskmanager.dto.TaskRequestDTO;
import br.com.agcapital.taskmanager.dto.TaskResponseDTO;
import br.com.agcapital.taskmanager.entity.Customer;
import br.com.agcapital.taskmanager.entity.Project;
import br.com.agcapital.taskmanager.entity.Task;
import br.com.agcapital.taskmanager.repository.TaskRepository;
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
class TaskServiceTest {

    @Autowired
    private TaskService taskService;

    @MockBean
    private TaskRepository taskRepository;

    private Project project;

    private Customer customer;

    @BeforeEach
    void setUp() {
        customer = new Customer();
        project = new Project();
        customer.setId(1L);
        customer.setCustomerName("Customer 1");
        project.setId(1L);
        project.setProjectName("Project 1");
        project.setCustomer(customer);
        customer.setProjects(List.of(project));
    }

    @Test
    void saveTask_newTask_shouldSaveTask() {
        TaskRequestDTO dto = new TaskRequestDTO();
        dto.setTaskName("Task 1");
        dto.setTaskDescription("Description 1");
        dto.setTaskStatus("Pending");

        Task task = new Task();
        task.setId(1L);
        task.setTaskName("Task 1");
        task.setTaskDescription("Description 1");
        task.setTaskStatus("Pending");
        task.setProject(project);

        when(taskRepository.save(any(Task.class))).thenReturn(task);

        Task savedTask = taskService.saveTask(dto, project);

        assertNotNull(savedTask);
        assertEquals("Task 1", savedTask.getTaskName());
        assertEquals("Description 1", savedTask.getTaskDescription());
        assertEquals("Pending", savedTask.getTaskStatus());
        assertEquals(project, savedTask.getProject());

        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    void saveTask_existingTask_shouldUpdateTask() {
        TaskRequestDTO dto = new TaskRequestDTO();
        dto.setId(1L);
        dto.setTaskName("Updated Task 1");
        dto.setTaskDescription("Updated Description 1");
        dto.setTaskStatus("Completed");

        Task existingTask = new Task();
        existingTask.setId(1L);
        existingTask.setTaskName("Task 1");
        existingTask.setTaskDescription("Description 1");
        existingTask.setTaskStatus("Pending");
        existingTask.setProject(project);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(existingTask));
        when(taskRepository.save(any(Task.class))).thenReturn(existingTask);

        Task updatedTask = taskService.saveTask(dto, project);

        assertNotNull(updatedTask);
        assertEquals("Updated Task 1", updatedTask.getTaskName());
        assertEquals("Updated Description 1", updatedTask.getTaskDescription());
        assertEquals("Completed", updatedTask.getTaskStatus());
        assertEquals(project, updatedTask.getProject());

        verify(taskRepository, times(1)).findById(1L);
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    void findById_existingId_shouldReturnTask() {
        Task task = new Task();
        task.setId(1L);
        task.setTaskName("Task 1");

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        Task foundTask = taskService.findById(1L);

        assertNotNull(foundTask);
        assertEquals(1L, foundTask.getId());
        assertEquals("Task 1", foundTask.getTaskName());

        verify(taskRepository, times(1)).findById(1L);
    }

    @Test
    void findById_nonExistingId_shouldReturnNull() {
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        Task foundTask = taskService.findById(1L);

        assertNull(foundTask);

        verify(taskRepository, times(1)).findById(1L);
    }

    @Test
    void listByProjectId_existingProjectId_shouldReturnTasks() {
        Task task1 = new Task();
        task1.setId(1L);
        task1.setTaskName("Task 1");
        task1.setProject(project);

        Task task2 = new Task();
        task2.setId(2L);
        task2.setTaskName("Task 2");
        task2.setProject(project);

        List<Task> tasks = List.of(task1, task2);

        when(taskRepository.findByProjectId(1L)).thenReturn(tasks);

        List<TaskResponseDTO> taskResponseDTOList = taskService.listByProjectId(1L);

        assertNotNull(taskResponseDTOList);
        assertEquals(2, taskResponseDTOList.size());

        verify(taskRepository, times(1)).findByProjectId(1L);
    }

    @Test
    void listTasks_shouldReturnAllTasks() {
        Task task1 = new Task();
        task1.setId(1L);
        task1.setTaskName("Task 1");

        Task task2 = new Task();
        task2.setId(2L);
        task2.setTaskName("Task 2");

        List<Task> tasks = List.of(task1, task2);

        when(taskRepository.findAll()).thenReturn(tasks);

        List<Task> taskList = taskService.listTasks();

        assertNotNull(taskList);
        assertEquals(2, taskList.size());

        verify(taskRepository, times(1)).findAll();
    }

    @Test
    void deleteTask_existingId_shouldDeleteTask() {
        Long taskId = 1L;

        doNothing().when(taskRepository).deleteById(taskId);

        taskService.deleteTask(taskId);

        verify(taskRepository, times(1)).deleteById(taskId);
    }
}