package br.com.legalflow.controller;

import br.com.legalflow.dto.request.QuadroRequestDTO;
import br.com.legalflow.entity.Quadro;
import br.com.legalflow.entity.Usuario;
import br.com.legalflow.enums.RoleEnum;
import br.com.legalflow.service.OrganizacaoService;
import br.com.legalflow.service.QuadroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/quadro")
public class QuadroController extends BaseController {

    @Autowired
    private QuadroService quadroService;
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

}
