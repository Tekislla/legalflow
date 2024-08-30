package br.com.legalflow.controller;

import br.com.legalflow.dto.ProjectRequestDTO;
import br.com.legalflow.dto.ProjectResponseDTO;
import br.com.legalflow.dto.TaskResponseDTO;
import br.com.legalflow.entity.Customer;
import br.com.legalflow.entity.Project;
import br.com.legalflow.service.CustomerService;
import br.com.legalflow.service.ProjectService;
import br.com.legalflow.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/projects")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private TaskService taskService;

    @PostMapping
    public ResponseEntity<Project> saveProject(@RequestBody ProjectRequestDTO req) {
        Customer customer = customerService.findById(req.getCustomerId());
        Project project = projectService.saveProject(req, customer);
        return ResponseEntity.ok(project);
    }

    @GetMapping
    public ResponseEntity<List<ProjectResponseDTO>> listProjects() {
        List<ProjectResponseDTO> projects = projectService.listProjects();
        return ResponseEntity.ok(projects);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Project> getProjectById(@PathVariable Long id) {
        Project project = projectService.findById(id);
        if (project != null) {
            return ResponseEntity.ok(project);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        List<TaskResponseDTO> tasks = taskService.listByProjectId(id);
        for (TaskResponseDTO task : tasks) {
            taskService.deleteTask(task.getId());
        }
        projectService.deleteProject(id);
        return ResponseEntity.noContent().build();
    }
}
