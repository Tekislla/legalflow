package br.com.legalflow.controller;

import br.com.legalflow.controller.base.BaseController;
import br.com.legalflow.dto.request.QuadroRequestDTO;
import br.com.legalflow.entity.Quadro;
import br.com.legalflow.entity.Usuario;
import br.com.legalflow.enums.RoleEnum;
import br.com.legalflow.exception.usuario.UsuarioNaoAutorizadoException;
import br.com.legalflow.service.QuadroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/quadro")
public class QuadroController extends BaseController {

    @Autowired
    private QuadroService quadroService;

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
                throw new UsuarioNaoAutorizadoException();
            }

            Quadro quadro = quadroService.findById(id);
            quadroService.deletarQuadro(quadro);

            return ResponseEntity.ok("Quadro deletado com sucesso");
        } catch (Exception e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

}
