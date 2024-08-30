package br.com.legalflow.service;

import br.com.legalflow.dto.request.CadastroRequestDTO;
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
public class OrganizacaoService {

    @Autowired
    private OrganizacaoRepository organizacaoRepository;

    public Organizacao cadastrarOrganizacao(CadastroRequestDTO dto) {
        try {
            Organizacao organizacao = new Organizacao();

            organizacao.setNome(dto.getNomeOrganizacao());
            organizacao.setDocumento(dto.getDocumentoOrganizacao());

            return organizacaoRepository.save(organizacao);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Organizacao findById(Long id) {
        return organizacaoRepository.findById(id).orElseThrow(() -> new RuntimeException("Organização não encontrada"));
    }
}
