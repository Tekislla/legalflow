package br.com.legalflow.old.dto;

import br.com.legalflow.old.entity.Task;
import lombok.Data;

import java.util.List;

@Data
public class ProjectResponseDTO {
    private Long id;
    private String projectName;
    private String customerName;
    private List<Task> tasks;
}
