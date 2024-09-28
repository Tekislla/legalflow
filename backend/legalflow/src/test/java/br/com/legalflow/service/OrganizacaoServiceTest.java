package br.com.legalflow.service;

import br.com.legalflow.dto.request.CadastroRequestDTO;
import br.com.legalflow.entity.Organizacao;
import br.com.legalflow.exception.organizacao.OrganizacaoJaCadastradaException;
import br.com.legalflow.repository.OrganizacaoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrganizacaoServiceTest {

    @InjectMocks
    private OrganizacaoService organizacaoService;

    @Mock
    private OrganizacaoRepository organizacaoRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void shouldCadastrarOrganizacao() {
        CadastroRequestDTO dto = new CadastroRequestDTO();
        dto.setNomeOrganizacao("Org Teste");
        dto.setDocumentoOrganizacao("123456");

        when(organizacaoRepository.findByDocumento(dto.getDocumentoOrganizacao())).thenReturn(Optional.empty());
        when(organizacaoRepository.save(any(Organizacao.class))).thenReturn(new Organizacao());

        Organizacao organizacao = organizacaoService.cadastrarOrganizacao(dto);

        assertNotNull(organizacao);
        verify(organizacaoRepository, times(1)).save(any(Organizacao.class));
    }

    @Test
    void shouldThrowOrganizacaoJaCadastradaException() {
        CadastroRequestDTO dto = new CadastroRequestDTO();
        dto.setDocumentoOrganizacao("123456");

        when(organizacaoRepository.findByDocumento(dto.getDocumentoOrganizacao())).thenReturn(Optional.of(new Organizacao()));

        assertThrows(OrganizacaoJaCadastradaException.class, () -> {
            organizacaoService.cadastrarOrganizacao(dto);
        });
    }

    @Test
    void shouldFindById() {
        Organizacao organizacao = new Organizacao();
        organizacao.setId(1L);

        when(organizacaoRepository.findById(1L)).thenReturn(Optional.of(organizacao));

        Organizacao result = organizacaoService.findById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }
}
