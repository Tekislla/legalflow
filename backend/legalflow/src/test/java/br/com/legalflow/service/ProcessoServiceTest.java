package br.com.legalflow.service;

import br.com.legalflow.dto.request.ProcessoRequestDTO;
import br.com.legalflow.dto.response.DashboardInfoResponseDTO;
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

import java.util.List;
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
    void shouldThrowNullPointerExceptionOnSaveProcesso() {
        ProcessoRequestDTO dto = new ProcessoRequestDTO();
        dto.setQuadroId(1L);
        dto.setNumero("123456");
        dto.setPrazoFatal("01/01/2020");
        dto.setPrazoSubsidio("02/02/2021");
        dto.setStatus("TESTE");

        when(quadroRepository.findById(1L)).thenReturn(Optional.of(new Quadro()));
        when(processoRepository.save(any(Processo.class))).thenThrow(NullPointerException.class);

        assertThrows(NullPointerException.class, () -> {
            processoService.saveProcesso(dto, null);
        });
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

    @Test
    void shouldGetDashboardInfoAsAdmin() {
        when(processoRepository.countByQuadroOrganizacaoIdAndStatusIn(anyLong(), anyList())).thenReturn(5L);
        when(processoRepository.findByQuadroOrganizacaoIdAndPrazoSubsidioLessThanEqualAndStatusIn(anyLong(), any(), anyList())).thenReturn(List.of(new Processo()));
        when(processoRepository.findByQuadroOrganizacaoIdAndPrazoFatalLessThanEqualAndStatusIn(anyLong(), any(), anyList())).thenReturn(List.of(new Processo()));

        DashboardInfoResponseDTO dashboardInfo = processoService.getDashboardInfo(true, 1L);

        assertNotNull(dashboardInfo);
        assertEquals(5L, dashboardInfo.getTotalProcessosEmAberto());
        assertEquals(1, dashboardInfo.getProcessosAVencerPrazoSubsidio().size());
        assertEquals(1, dashboardInfo.getProcessosAVencerPrazoFatal().size());
    }

    @Test
    void shouldGetDashboardInfoAsNonAdmin() {
        when(processoRepository.countByQuadroUsuarioIdAndStatusIn(anyLong(), anyList())).thenReturn(3L);
        when(processoRepository.findByQuadroUsuarioIdAndPrazoSubsidioLessThanEqualAndStatusIn(anyLong(), any(), anyList())).thenReturn(List.of(new Processo()));
        when(processoRepository.findByQuadroUsuarioIdAndPrazoFatalLessThanEqualAndStatusIn(anyLong(), any(), anyList())).thenReturn(List.of(new Processo()));

        DashboardInfoResponseDTO dashboardInfo = processoService.getDashboardInfo(false, 1L);

        assertNotNull(dashboardInfo);
        assertEquals(3L, dashboardInfo.getTotalProcessosEmAberto());
        assertEquals(1, dashboardInfo.getProcessosAVencerPrazoSubsidio().size());
        assertEquals(1, dashboardInfo.getProcessosAVencerPrazoFatal().size());
    }

    @Test
    void shouldCountProcessosEmAbertoAsAdmin() {
        when(processoRepository.countByQuadroOrganizacaoIdAndStatusIn(anyLong(), anyList())).thenReturn(10L);

        long count = processoService.countProcessosEmAberto(true, 1L);

        assertEquals(10L, count);
    }

    @Test
    void shouldCountProcessosEmAbertoAsNonAdmin() {
        when(processoRepository.countByQuadroUsuarioIdAndStatusIn(anyLong(), anyList())).thenReturn(7L);

        long count = processoService.countProcessosEmAberto(false, 1L);

        assertEquals(7L, count);
    }

    @Test
    void shouldFindProcessosAVencerByPrazoSubsidioAsAdmin() {
        Processo processo = new Processo();
        when(processoRepository.findByQuadroOrganizacaoIdAndPrazoSubsidioLessThanEqualAndStatusIn(anyLong(), any(), anyList())).thenReturn(List.of(processo));

        List<Processo> processos = processoService.findProcessosAVencerByPrazoSubsidio(true, 1L);

        assertNotNull(processos);
        assertEquals(1, processos.size());
    }

    @Test
    void shouldFindProcessosAVencerByPrazoSubsidioAsNonAdmin() {
        Processo processo = new Processo();
        when(processoRepository.findByQuadroUsuarioIdAndPrazoSubsidioLessThanEqualAndStatusIn(anyLong(), any(), anyList())).thenReturn(List.of(processo));

        List<Processo> processos = processoService.findProcessosAVencerByPrazoSubsidio(false, 1L);

        assertNotNull(processos);
        assertEquals(1, processos.size());
    }

    @Test
    void shouldFindProcessosAVencerByPrazoFatalAsAdmin() {
        Processo processo = new Processo();
        when(processoRepository.findByQuadroOrganizacaoIdAndPrazoFatalLessThanEqualAndStatusIn(anyLong(), any(), anyList())).thenReturn(List.of(processo));

        List<Processo> processos = processoService.findProcessosAVencerByPrazoFatal(true, 1L);

        assertNotNull(processos);
        assertEquals(1, processos.size());
    }

    @Test
    void shouldFindProcessosAVencerByPrazoFatalAsNonAdmin() {
        Processo processo = new Processo();
        when(processoRepository.findByQuadroUsuarioIdAndPrazoFatalLessThanEqualAndStatusIn(anyLong(), any(), anyList())).thenReturn(List.of(processo));

        List<Processo> processos = processoService.findProcessosAVencerByPrazoFatal(false, 1L);

        assertNotNull(processos);
        assertEquals(1, processos.size());
    }

    @Test
    void shouldDeleteProcessoById() {
        processoService.deleteById(1L);

        verify(processoRepository, times(1)).deleteById(1L);
    }
}
