package br.com.agcapital.taskmanager.dto;

import br.com.agcapital.taskmanager.entity.Task;
import lombok.Data;

import java.util.List;

@Data
public class ProjectResponseDTO {
    private Long id;
    private String projectName;
    private String customerName;
    private List<Task> tasks;
}
