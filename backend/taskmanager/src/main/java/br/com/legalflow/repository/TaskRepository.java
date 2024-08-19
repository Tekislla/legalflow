package br.com.agcapital.taskmanager.repository;

import br.com.agcapital.taskmanager.entity.Task;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TaskRepository extends CrudRepository<Task, Long> {
    List<Task> findByProjectId(Long projectId);
}
