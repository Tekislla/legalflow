package br.com.legalflow.controller;

import br.com.legalflow.dto.request.QuadroRequestDTO;
import br.com.legalflow.entity.Quadro;
import br.com.legalflow.entity.Usuario;
import br.com.legalflow.enums.RoleEnum;
import br.com.legalflow.service.OrganizacaoService;
import br.com.legalflow.service.ProcessoService;
import br.com.legalflow.service.QuadroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/quadro")
public class QuadroController extends BaseController {

    @Autowired
    private QuadroService quadroService;
    @Autowired
    private ProcessoService processoService;
    @Autowired
    private OrganizacaoService organizacaoService;

    @PostMapping("/")
    public ResponseEntity<?> criarQuadro(@RequestBody QuadroRequestDTO quadroRequestDTO) {
        try {
            Usuario usuarioLogado = getUsuarioLogado();
            quadroRequestDTO.setOrganizacaoId(usuarioLogado.getOrganizacao().getId());
            Quadro quadro = quadroService.saveQuadro(quadroRequestDTO);

            return ResponseEntity.ok(quadro);
        } catch (Exception e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletarQuadro(@PathVariable Long id) {
        try {
            Usuario usuarioLogado = getUsuarioLogado();

            if (usuarioLogado.getRole().equals(RoleEnum.USER.toString())) {
                throw new Exception("Você não tem permissão para deletar este quadro");
            }

            Quadro quadro = quadroService.findById(id);

            for (var processo : quadro.getProcessos()) {
                processoService.deleteById(processo.getId());
            }

            quadroService.deleteById(id);

            return ResponseEntity.ok("Quadro deletado com sucesso");
        } catch (Exception e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

}
