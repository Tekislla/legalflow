package br.com.legalflow.repository;

import br.com.legalflow.entity.Processo;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;
import java.util.List;

public interface ProcessoRepository extends CrudRepository<Processo, Long> {

    long countByQuadroOrganizacaoId(Long organizacaoId);

    List<Processo> findByQuadroOrganizacaoIdAndPrazoSubsidioLessThanEqual(Long organizacaoId, Date prazoSubsidioLimite);

    List<Processo> findByQuadroOrganizacaoIdAndPrazoFatalLessThanEqual(Long organizacaoId, Date prazoFatalLimite);
}
