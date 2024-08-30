package br.com.legalflow.service;

import br.com.legalflow.dto.request.ProcessoRequestDTO;
import br.com.legalflow.entity.Processo;
import br.com.legalflow.entity.Quadro;
import br.com.legalflow.repository.ProcessoRepository;
import br.com.legalflow.repository.QuadroRepository;
import br.com.legalflow.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public Processo saveProcesso(ProcessoRequestDTO dto) {
        try {
            Processo processo = new Processo();

            if (dto.getId() != null) {
                processo = findById(dto.getId());
            }

            Optional<Quadro> quadroOpt = quadroRepository.findById(dto.getQuadroId());

            if (quadroOpt.isEmpty()) {
                throw new Exception("Quadro não encontrado");
            }

            processo.setQuadro(quadroOpt.get());
            processo.setNome(dto.getNome());
            processo.setDescricao(dto.getDescricao());
            processo.setNumero(dto.getNumero());
            processo.setDescricao(dto.getDescricao());
            processo.setPrazoSubsidio(dto.getPrazoSubsidio());
            processo.setPrazoFatal(dto.getPrazoFatal());
            processo.setArquivo(dto.getArquivo());

            return processoRepository.save(processo);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Processo findById(Long id) {
        return processoRepository.findById(id).orElseThrow(() -> new RuntimeException("Processo não encontrado"));
    }

    public List<Processo> findByQuadroId(Long quadroId) {
        return processoRepository.findByQuadroId(quadroId);
    }

    public void deleteById(Long id) {
        processoRepository.deleteById(id);
    }
}
