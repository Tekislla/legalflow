package br.com.legalflow.service;

import br.com.legalflow.dto.request.CadastroRequestDTO;
import br.com.legalflow.entity.Organizacao;
import br.com.legalflow.exception.organizacao.OrganizacaoJaCadastradaException;
import br.com.legalflow.exception.organizacao.OrganizacaoNaoEncontradaException;
import br.com.legalflow.repository.OrganizacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OrganizacaoService {

    @Autowired
    private OrganizacaoRepository organizacaoRepository;

    public Organizacao cadastrarOrganizacao(CadastroRequestDTO dto) {
        if (dto.getOrganizacaoId() != null) {
            return findById(dto.getOrganizacaoId());
        }

        Optional<Organizacao> organizacaoOpt = organizacaoRepository.findByDocumento(dto.getDocumentoOrganizacao());

        if (organizacaoOpt.isPresent()) {
            throw new OrganizacaoJaCadastradaException(dto.getDocumentoOrganizacao());
        }

        Organizacao organizacao = new Organizacao();

        organizacao.setNome(dto.getNomeOrganizacao());
        organizacao.setDocumento(dto.getDocumentoOrganizacao());

        return organizacaoRepository.save(organizacao);
    }

    public Organizacao findById(Long id) {
        return organizacaoRepository.findById(id).orElseThrow(() -> new OrganizacaoNaoEncontradaException(id));
    }

    public Optional<Organizacao> findByDocumento(String documento) {
        return organizacaoRepository.findByDocumento(documento);
    }
}
