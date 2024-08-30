package br.com.legalflow.old.dto;

import lombok.Data;

@Data
public class TaskResponseDTO {
    private Long id;
    private String taskName;
    private String taskDescription;
    private String taskStatus;
    private String projectName;
    private String customerName;
}
