package br.com.agcapital.taskmanager.dto;

import lombok.Data;

@Data
public class TaskRequestDTO {
    private Long id;
    private String taskName;
    private String taskDescription;
    private String taskStatus;
    private Long projectId;
}
