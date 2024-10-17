package br.com.legalflow.service;

import br.com.legalflow.dto.request.ProcessoRequestDTO;
import br.com.legalflow.entity.Processo;
import br.com.legalflow.entity.Quadro;
import br.com.legalflow.exception.quadro.QuadroNaoEncontradoException;
import br.com.legalflow.repository.ProcessoRepository;
import br.com.legalflow.repository.QuadroRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProcessoServiceTest {

    @InjectMocks
    private ProcessoService processoService;
    @Mock
    private ProcessoRepository processoRepository;
    @Mock
    private QuadroRepository quadroRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void shouldSaveProcesso() {
        ProcessoRequestDTO dto = new ProcessoRequestDTO();
        dto.setQuadroId(1L);
        dto.setNumero("123456");
        dto.setPrazoFatal("01/01/2020");
        dto.setPrazoSubsidio("02/02/2021");
        dto.setStatus("CRIADO");

        when(quadroRepository.findById(1L)).thenReturn(Optional.of(new Quadro()));
        when(processoRepository.save(any(Processo.class))).thenReturn(new Processo());

        Processo processo = processoService.saveProcesso(dto, null);

        assertNotNull(processo);
        verify(processoRepository, times(1)).save(any(Processo.class));
    }

    @Test
    void shouldThrowQuadroNaoEncontradoException() {
        ProcessoRequestDTO dto = new ProcessoRequestDTO();
        dto.setQuadroId(1L);

        when(quadroRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(QuadroNaoEncontradoException.class, () -> {
            processoService.saveProcesso(dto, null);
        });
    }

    @Test
    void shouldFindById() {
        Processo processo = new Processo();
        processo.setId(1L);

        when(processoRepository.findById(1L)).thenReturn(Optional.of(processo));

        Processo processoEncontrado = processoService.findById(1L);

        assertNotNull(processoEncontrado);
        assertEquals(1L, processoEncontrado.getId());
    }
}
