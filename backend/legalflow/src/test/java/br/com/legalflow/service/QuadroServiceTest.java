package br.com.legalflow.service;

import br.com.legalflow.dto.request.QuadroRequestDTO;
import br.com.legalflow.entity.Organizacao;
import br.com.legalflow.entity.Quadro;
import br.com.legalflow.entity.Usuario;
import br.com.legalflow.exception.quadro.QuadroNaoEncontradoException;
import br.com.legalflow.exception.usuario.UsuarioNaoEncontradoException;
import br.com.legalflow.repository.OrganizacaoRepository;
import br.com.legalflow.repository.QuadroRepository;
import br.com.legalflow.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class QuadroServiceTest {

    @InjectMocks
    private QuadroService quadroService;
    @Mock
    private QuadroRepository quadroRepository;
    @Mock
    private UsuarioRepository usuarioRepository;
    @Mock
    private OrganizacaoRepository organizacaoRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void shouldSaveQuadro() {
        QuadroRequestDTO dto = new QuadroRequestDTO();
        dto.setUsuarioId(1L);
        dto.setOrganizacaoId(1L);
        dto.setNome("Novo Quadro");

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(new Usuario()));
        when(organizacaoRepository.findById(1L)).thenReturn(Optional.of(new Organizacao()));
        when(quadroRepository.save(any(Quadro.class))).thenReturn(new Quadro());

        Quadro quadro = quadroService.saveQuadro(dto);

        assertNotNull(quadro);
        verify(quadroRepository, times(1)).save(any(Quadro.class));
    }

    @Test
    void shouldThrowUsuarioNaoEncontradoException() {
        QuadroRequestDTO dto = new QuadroRequestDTO();
        dto.setUsuarioId(1L);

        when(usuarioRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UsuarioNaoEncontradoException.class, () -> {
            quadroService.saveQuadro(dto);
        });
    }

    @Test
    void shouldFindById() {
        Quadro quadro = new Quadro();
        quadro.setId(1L);

        when(quadroRepository.findById(1L)).thenReturn(Optional.of(quadro));

        Quadro result = quadroService.findById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void shouldThrowQuadroNaoEncontradoException() {
        when(quadroRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(QuadroNaoEncontradoException.class, () -> {
            quadroService.findById(1L);
        });
    }
}
