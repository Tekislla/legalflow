package br.com.agcapital.taskmanager.dto;

import lombok.Data;

@Data
public class ProjectRequestDTO {
    private Long id;
    private String projectName;
    private Long customerId;
}
