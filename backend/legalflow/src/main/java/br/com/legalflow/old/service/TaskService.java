package br.com.legalflow.old.service;

import br.com.legalflow.old.dto.TaskRequestDTO;
import br.com.legalflow.old.dto.TaskResponseDTO;
import br.com.legalflow.old.entity.Project;
import br.com.legalflow.old.entity.Task;
import br.com.legalflow.old.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    public Task saveTask(TaskRequestDTO dto, Project project) {
        if (dto.getId() != null) {
            Task task = findById(dto.getId());
            if (task != null) {
                task.setTaskName(dto.getTaskName());
                task.setTaskDescription(dto.getTaskDescription());
                task.setTaskStatus(dto.getTaskStatus());
                task.setProject(project);
                return taskRepository.save(task);
            }
        }
        Task task = new Task();
        task.setTaskName(dto.getTaskName());
        task.setTaskDescription(dto.getTaskDescription());
        task.setTaskStatus(dto.getTaskStatus());
        task.setProject(project);
        return taskRepository.save(task);
    }

    public Task findById(Long id) {
        return taskRepository.findById(id).orElse(null);
    }

    public List<TaskResponseDTO> listByProjectId(Long projectId) {
        List<TaskResponseDTO> returnTaskList = new ArrayList<>();
        List<Task> tasks = taskRepository.findByProjectId(projectId);

        for (Task task : tasks) {
            TaskResponseDTO taskResponseDTO = new TaskResponseDTO();
            taskResponseDTO.setId(task.getId());
            taskResponseDTO.setTaskName(task.getTaskName());
            taskResponseDTO.setTaskDescription(task.getTaskDescription());
            taskResponseDTO.setTaskStatus(task.getTaskStatus());
            taskResponseDTO.setProjectName(task.getProject().getProjectName());
            taskResponseDTO.setCustomerName(task.getProject().getCustomer().getCustomerName());
            returnTaskList.add(taskResponseDTO);
        }

        return returnTaskList;
    }

    public List<Task> listTasks() {
        return (List<Task>) taskRepository.findAll();
    }

    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }

}
