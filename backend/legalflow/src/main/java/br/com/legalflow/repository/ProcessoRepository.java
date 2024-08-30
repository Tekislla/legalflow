package br.com.legalflow.repository;

import br.com.legalflow.entity.Processo;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ProcessoRepository extends CrudRepository<Processo, Long> {
    List<Processo> findByQuadroId(Long quadroId);
}
