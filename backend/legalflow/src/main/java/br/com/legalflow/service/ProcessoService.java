package br.com.legalflow.service;

import br.com.legalflow.dto.request.ProcessoRequestDTO;
import br.com.legalflow.entity.Processo;
import br.com.legalflow.entity.Quadro;
import br.com.legalflow.exception.processo.ProcessoNaoEncontradoException;
import br.com.legalflow.exception.quadro.QuadroNaoEncontradoException;
import br.com.legalflow.repository.ProcessoRepository;
import br.com.legalflow.repository.QuadroRepository;
import br.com.legalflow.repository.UsuarioRepository;
import br.com.legalflow.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ProcessoService {

    @Autowired
    private ProcessoRepository processoRepository;
    @Autowired
    private QuadroRepository quadroRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;


    public Processo saveProcesso(ProcessoRequestDTO dto, byte[] arquivo) {
        try {
            Processo processo = new Processo();

            if (dto.getId() != null) {
                processo = findById(dto.getId());
            }

            Optional<Quadro> quadroOpt = quadroRepository.findById(dto.getQuadroId());

            if (quadroOpt.isEmpty()) {
                throw new QuadroNaoEncontradoException(dto.getQuadroId());
            }

            Date prazoSubsidio = DateUtils.parseDate(dto.getPrazoSubsidio(), "dd/MM/yyyy");
            Date prazoFatal = DateUtils.parseDate(dto.getPrazoFatal(), "dd/MM/yyyy");

            processo.setQuadro(quadroOpt.get());
            processo.setNome(dto.getNome());
            processo.setDescricao(dto.getDescricao());
            processo.setNumero(dto.getNumero());
            processo.setAutor(dto.getAutor());
            processo.setReu(dto.getReu());
            processo.setStatus("CRIADO");
            processo.setDescricao(dto.getDescricao());
            processo.setPrazoSubsidio(prazoSubsidio);
            processo.setPrazoFatal(prazoFatal);
            processo.setArquivo(arquivo);

            return processoRepository.save(processo);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Processo findById(Long id) {
        return processoRepository.findById(id).orElseThrow(() -> new ProcessoNaoEncontradoException(id));
    }

    public List<Processo> findByQuadroId(Long quadroId) {
        return processoRepository.findByQuadroId(quadroId);
    }

    public void deleteById(Long id) {
        processoRepository.deleteById(id);
    }
}
