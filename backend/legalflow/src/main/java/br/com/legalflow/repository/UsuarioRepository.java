package br.com.legalflow.repository;

import br.com.legalflow.entity.Usuario;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends CrudRepository<Usuario, Long> {

    Optional<Usuario> findByEmail(String email);
    List<Usuario> findByOrganizacaoId(Long organizacaoId);
}
