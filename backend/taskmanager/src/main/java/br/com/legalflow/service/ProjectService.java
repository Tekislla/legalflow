package br.com.agcapital.taskmanager.service;

import br.com.agcapital.taskmanager.dto.ProjectRequestDTO;
import br.com.agcapital.taskmanager.dto.ProjectResponseDTO;
import br.com.agcapital.taskmanager.entity.Customer;
import br.com.agcapital.taskmanager.entity.Project;
import br.com.agcapital.taskmanager.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    public Project saveProject(ProjectRequestDTO dto, Customer customer) {
        if (dto.getId() != null) {
            Project project = findById(dto.getId());
            if (project != null) {
                project.setProjectName(dto.getProjectName());
                return projectRepository.save(project);
            }
        }
        Project project = new Project();
        project.setProjectName(dto.getProjectName());
        project.setCustomer(customer);
        return projectRepository.save(project);
    }

    public Project findById(Long id) {
        return projectRepository.findById(id).orElse(null);
    }

    public List<ProjectResponseDTO> listProjects() {
        List<ProjectResponseDTO> returnProjectList = new ArrayList<>();
        List<Project> list = (List<Project>) projectRepository.findAll();

        for (Project project : list) {
            ProjectResponseDTO projectResponseDTO = new ProjectResponseDTO();
            projectResponseDTO.setId(project.getId());
            projectResponseDTO.setProjectName(project.getProjectName());
            projectResponseDTO.setCustomerName(project.getCustomer().getCustomerName());
            projectResponseDTO.setTasks(project.getTasks());
            returnProjectList.add(projectResponseDTO);
        }

        return returnProjectList;
    }

    public void deleteProject(Long id) {
        projectRepository.deleteById(id);
    }

}
