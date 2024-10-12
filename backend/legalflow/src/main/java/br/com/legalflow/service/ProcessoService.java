package br.com.legalflow.service;

import br.com.legalflow.dto.request.ProcessoRequestDTO;
import br.com.legalflow.dto.response.DashboardInfoResponseDTO;
import br.com.legalflow.entity.Processo;
import br.com.legalflow.entity.Quadro;
import br.com.legalflow.enums.ProcessoStatusEnum;
import br.com.legalflow.exception.processo.ProcessoNaoEncontradoException;
import br.com.legalflow.exception.quadro.QuadroNaoEncontradoException;
import br.com.legalflow.repository.ProcessoRepository;
import br.com.legalflow.repository.QuadroRepository;
import br.com.legalflow.repository.UsuarioRepository;
import br.com.legalflow.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Stream;

@Service
public class ProcessoService {

    @Autowired
    private ProcessoRepository processoRepository;
    @Autowired
    private QuadroRepository quadroRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;

    private final List<String> listaStatusEmAberto = List.of(ProcessoStatusEnum.CRIADO.name(), ProcessoStatusEnum.EM_PROGRESSO.name());


    public Processo saveProcesso(ProcessoRequestDTO dto, byte[] arquivo) {
        Processo processo = new Processo();

        if (dto.getId() != null) {
            processo = findById(dto.getId());
        }

        Optional<Quadro> quadroOpt = quadroRepository.findById(dto.getQuadroId());

        if (quadroOpt.isEmpty()) {
            throw new QuadroNaoEncontradoException(dto.getQuadroId());
        }

        Date prazoSubsidio = DateUtils.getDataFormatada(dto.getPrazoSubsidio(), "dd/MM/yyyy");
        Date prazoFatal = DateUtils.getDataFormatada(dto.getPrazoFatal(), "dd/MM/yyyy");
        ProcessoStatusEnum status = ProcessoStatusEnum.getProcessoStatusEnum(dto.getStatus());

        processo.setQuadro(quadroOpt.get());
        processo.setNome(dto.getNome());
        processo.setDescricao(dto.getDescricao());
        processo.setNumero(dto.getNumero());
        processo.setAutor(dto.getAutor());
        processo.setReu(dto.getReu());
        processo.setStatus(status.name());
        processo.setDescricao(dto.getDescricao());
        processo.setPrazoSubsidio(prazoSubsidio);
        processo.setPrazoFatal(prazoFatal);
        processo.setArquivo(arquivo);

        return processoRepository.save(processo);
    }

    public Processo findById(Long id) {
        return processoRepository.findById(id).orElseThrow(() -> new ProcessoNaoEncontradoException(id));
    }

    public DashboardInfoResponseDTO getDashboardInfo(Long organizacaoId) {
        DashboardInfoResponseDTO dashboardInfo = new DashboardInfoResponseDTO();
        Set<Long> idsProcessos = new HashSet<>();

        List<Processo> processosAVencerPrazoSubsidio = findProcessosAVencerByPrazoSubsidio(organizacaoId);
        List<Processo> processosAVencerPrazoFatal = findProcessosAVencerByPrazoFatal(organizacaoId);
        List<Processo> processosAVencer = Stream.concat(processosAVencerPrazoSubsidio.stream(), processosAVencerPrazoFatal.stream())
                .filter(processo -> idsProcessos.add(processo.getId()))
                .toList();


        dashboardInfo.setTotalProcessosEmAberto(countProcessosEmAbertoByOrganizacaoId(organizacaoId));
        dashboardInfo.setProcessosAVencerPrazoSubsidio(processosAVencerPrazoSubsidio);
        dashboardInfo.setProcessosAVencerPrazoFatal(processosAVencerPrazoFatal);
        dashboardInfo.setProcessosAVencer(processosAVencer);

        return dashboardInfo;
    }

    public long countProcessosEmAbertoByOrganizacaoId(Long organizacaoId) {
       return processoRepository.countByQuadroOrganizacaoIdAndStatusIn(organizacaoId, listaStatusEmAberto);
    }

    public List<Processo> findProcessosAVencerByPrazoSubsidio(Long organizacaoId) {
        Date prazo = DateUtils.getDataSomandoDias(7);
        return processoRepository.findByQuadroOrganizacaoIdAndPrazoSubsidioLessThanEqualAndStatusIn(organizacaoId, prazo, listaStatusEmAberto);
    }

    public List<Processo> findProcessosAVencerByPrazoFatal(Long organizacaoId) {
        Date prazo = DateUtils.getDataSomandoDias(7);
        return processoRepository.findByQuadroOrganizacaoIdAndPrazoFatalLessThanEqualAndStatusIn(organizacaoId, prazo, listaStatusEmAberto);
    }

    public void deleteById(Long id) {
        processoRepository.deleteById(id);
    }
}
