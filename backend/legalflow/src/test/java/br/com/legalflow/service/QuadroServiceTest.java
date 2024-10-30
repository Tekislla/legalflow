package br.com.legalflow.service;

import br.com.legalflow.dto.request.QuadroRequestDTO;
import br.com.legalflow.entity.Organizacao;
import br.com.legalflow.entity.Processo;
import br.com.legalflow.entity.Quadro;
import br.com.legalflow.entity.Usuario;
import br.com.legalflow.exception.organizacao.OrganizacaoNaoEncontradaException;
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

import java.util.List;
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
    @Mock
    private ProcessoService processoService;

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

    @Test
    void shouldUpdateQuadroWhenIdExists() {
        QuadroRequestDTO dto = new QuadroRequestDTO();
        dto.setId(1L);
        dto.setUsuarioId(1L);
        dto.setOrganizacaoId(1L);
        dto.setNome("Quadro Atualizado");

        Quadro quadroExistente = new Quadro();
        quadroExistente.setId(1L);

        when(quadroRepository.findById(1L)).thenReturn(Optional.of(quadroExistente));
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(new Usuario()));
        when(organizacaoRepository.findById(1L)).thenReturn(Optional.of(new Organizacao()));
        when(quadroRepository.save(any(Quadro.class))).thenReturn(new Quadro());

        Quadro result = quadroService.saveQuadro(dto);

        assertNotNull(result);
        verify(quadroRepository, times(1)).findById(1L);
        verify(quadroRepository, times(1)).save(any(Quadro.class));
    }

    @Test
    void shouldThrowOrganizacaoNaoEncontradaException() {
        QuadroRequestDTO dto = new QuadroRequestDTO();
        dto.setUsuarioId(1L);
        dto.setOrganizacaoId(1L);

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(new Usuario()));
        when(organizacaoRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(OrganizacaoNaoEncontradaException.class, () -> {
            quadroService.saveQuadro(dto);
        });
    }

    @Test
    void shouldDeletarQuadroAndDeleteAssociatedProcessos() {
        Quadro quadro = new Quadro();
        quadro.setId(1L);

        Processo processo1 = new Processo();
        processo1.setId(101L);
        Processo processo2 = new Processo();
        processo2.setId(102L);
        quadro.setProcessos(List.of(processo1, processo2));
        when(quadroRepository.findById(1L)).thenReturn(Optional.of(quadro));
        doNothing().when(processoService).deleteById(anyLong());
        doNothing().when(quadroRepository).deleteById(1L);

        quadroService.deletarQuadro(quadro);

        verify(processoService).deleteById(101L);
        verify(processoService).deleteById(102L);
        verify(quadroRepository, times(1)).deleteById(1L);
    }
}
