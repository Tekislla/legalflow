package br.com.agcapital.taskmanager.controller;

import br.com.agcapital.taskmanager.dto.TaskRequestDTO;
import br.com.agcapital.taskmanager.dto.TaskResponseDTO;
import br.com.agcapital.taskmanager.entity.Project;
import br.com.agcapital.taskmanager.entity.Task;
import br.com.agcapital.taskmanager.service.ProjectService;
import br.com.agcapital.taskmanager.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private ProjectService projectService;

    @PostMapping
    public ResponseEntity<Task> saveTask(@RequestBody TaskRequestDTO req) {
        Project project = projectService.findById(req.getProjectId());
        Task task = taskService.saveTask(req, project);
        return ResponseEntity.ok(task);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<Task>> listTasks() {
        List<Task> tasks = taskService.listTasks();
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long id) {
        Task task = taskService.findById(id);
        if (task != null) {
            return ResponseEntity.ok(task);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<TaskResponseDTO>> listTasksByProjectId(@PathVariable Long projectId) {
        List<TaskResponseDTO> tasks = taskService.listByProjectId(projectId);
        return ResponseEntity.ok(tasks);
    }
}
