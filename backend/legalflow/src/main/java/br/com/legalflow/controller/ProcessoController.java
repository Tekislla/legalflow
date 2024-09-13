package br.com.legalflow.controller;

import br.com.legalflow.controller.base.BaseController;
import br.com.legalflow.dto.request.ProcessoRequestDTO;
import br.com.legalflow.entity.Processo;
import br.com.legalflow.entity.Usuario;
import br.com.legalflow.enums.RoleEnum;
import br.com.legalflow.service.OrganizacaoService;
import br.com.legalflow.service.ProcessoService;
import br.com.legalflow.service.QuadroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/processo")
public class ProcessoController extends BaseController {

    @Autowired
    private QuadroService quadroService;
    @Autowired
    private ProcessoService processoService;
    @Autowired
    private OrganizacaoService organizacaoService;

    @PostMapping("/")
    public ResponseEntity<?> criarProcesso(@RequestBody ProcessoRequestDTO processoRequestDTO) {
        try {
            Processo processo = processoService.saveProcesso(processoRequestDTO);

            return ResponseEntity.ok(processo);
        } catch (Exception e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletarProcesso(@PathVariable Long id) {
        try {
            Usuario usuarioLogado = getUsuarioLogado();

            if (usuarioLogado.getRole().equals(RoleEnum.USER.toString())) {
                throw new Exception("Você não tem permissão para deletar este processo");
            }

            processoService.deleteById(id);

            return ResponseEntity.ok("Quadro deletado com sucesso");
        } catch (Exception e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

}
