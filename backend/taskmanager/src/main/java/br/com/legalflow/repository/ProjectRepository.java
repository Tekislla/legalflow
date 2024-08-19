package br.com.agcapital.taskmanager.repository;

import br.com.agcapital.taskmanager.entity.Project;
import org.springframework.data.repository.CrudRepository;

public interface ProjectRepository extends CrudRepository<Project, Long> {
}
