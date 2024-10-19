package br.com.legalflow.repository;

import br.com.legalflow.entity.Processo;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;
import java.util.List;

public interface ProcessoRepository extends CrudRepository<Processo, Long> {

    long countByQuadroOrganizacaoIdAndStatusIn(Long organizacaoId, List<String> status);
    List<Processo> findByQuadroOrganizacaoIdAndPrazoSubsidioLessThanEqualAndStatusIn(Long organizacaoId, Date prazoSubsidioLimite, List<String> status);
    List<Processo> findByQuadroOrganizacaoIdAndPrazoFatalLessThanEqualAndStatusIn(Long organizacaoId, Date prazoFatalLimite, List<String> status);
    long countByQuadroUsuarioIdAndStatusIn(Long usuarioId, List<String> status);
    List<Processo> findByQuadroUsuarioIdAndPrazoSubsidioLessThanEqualAndStatusIn(Long usuarioId, Date prazoSubsidioLimite, List<String> status);
    List<Processo> findByQuadroUsuarioIdAndPrazoFatalLessThanEqualAndStatusIn(Long usuarioId, Date prazoFatalLimite, List<String> status);
}
