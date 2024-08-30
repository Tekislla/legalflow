package br.com.legalflow.old.repository;

import br.com.legalflow.old.entity.Project;
import org.springframework.data.repository.CrudRepository;

public interface ProjectRepository extends CrudRepository<Project, Long> {
}
