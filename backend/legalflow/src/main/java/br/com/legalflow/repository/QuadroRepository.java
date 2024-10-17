package br.com.legalflow.repository;

import br.com.legalflow.entity.Quadro;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface QuadroRepository extends CrudRepository<Quadro, Long> {

    List<Quadro> findByUsuarioIdAndOrganizacaoId(Long usuarioId, Long organizacaoId);
    List<Quadro> findByOrganizacaoId(Long organizacaoId);
}
