package br.com.legalflow.repository;

import br.com.legalflow.entity.Task;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TaskRepository extends CrudRepository<Task, Long> {
    List<Task> findByProjectId(Long projectId);
}
