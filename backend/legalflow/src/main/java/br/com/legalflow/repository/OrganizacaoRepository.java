package br.com.legalflow.repository;

import br.com.legalflow.entity.Organizacao;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface OrganizacaoRepository extends CrudRepository<Organizacao, Long> {
    Optional<Organizacao> findByDocumento(String documento);
}
