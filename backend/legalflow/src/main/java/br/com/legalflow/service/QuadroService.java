package br.com.legalflow.service;

import br.com.legalflow.dto.request.QuadroRequestDTO;
import br.com.legalflow.entity.Organizacao;
import br.com.legalflow.entity.Quadro;
import br.com.legalflow.entity.Usuario;
import br.com.legalflow.repository.OrganizacaoRepository;
import br.com.legalflow.repository.QuadroRepository;
import br.com.legalflow.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class QuadroService {

    @Autowired
    private QuadroRepository quadroRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private OrganizacaoRepository organizacaoRepository;

    public Quadro saveQuadro(QuadroRequestDTO dto) {
        try {
            Quadro quadro = new Quadro();

            if (dto.getId() != null) {
               quadro = findById(dto.getId());
            }

            Optional<Usuario> usuarioOpt = usuarioRepository.findById(dto.getUsuarioId());
            Optional<Organizacao> organizacaoOpt = organizacaoRepository.findById(dto.getOrganizacaoId());

            if (usuarioOpt.isEmpty()) {
                throw new Exception("Usuário não encontrado");
            }

            if (organizacaoOpt.isEmpty()) {
                throw new Exception("Organização não encontrada");
            }

            quadro.setUsuario(usuarioOpt.get());
            quadro.setOrganizacao(organizacaoOpt.get());
            quadro.setNome(dto.getNome());

            return quadroRepository.save(quadro);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Quadro findById(Long id) {
        return quadroRepository.findById(id).orElseThrow(() -> new RuntimeException("Quadro não encontrado"));
    }

    public List<Quadro> findByUsuarioIdAndOrganizacaoId(Long usuarioId, Long organizacaoId) {
        return quadroRepository.findByUsuarioIdAndOrganizacaoId(usuarioId, organizacaoId);
    }

    public List<Quadro> findByOrganizacaoId(Long organizacaoId) {
        return quadroRepository.findByOrganizacaoId(organizacaoId);
    }

    public void deleteById(Long id) {
        quadroRepository.deleteById(id);
    }
}
