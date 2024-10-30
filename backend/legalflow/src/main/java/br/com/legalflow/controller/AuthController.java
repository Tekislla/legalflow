package br.com.legalflow.controller;

import br.com.legalflow.controller.base.BaseController;
import br.com.legalflow.dto.request.CadastroRequestDTO;
import br.com.legalflow.dto.request.LoginRequestDTO;
import br.com.legalflow.dto.response.LoginResponseDTO;
import br.com.legalflow.entity.Organizacao;
import br.com.legalflow.entity.Usuario;
import br.com.legalflow.service.OrganizacaoService;
import br.com.legalflow.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController extends BaseController {

    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private OrganizacaoService organizacaoService;

    @PostMapping("/cadastro")
    public ResponseEntity<?> cadastrar(@RequestBody CadastroRequestDTO cadastroRequestDTO) {
        try {
            Organizacao organizacao = organizacaoService.cadastrarOrganizacao(cadastroRequestDTO);
            Usuario usuario = usuarioService.cadastrarUsuario(cadastroRequestDTO, organizacao);
            String token = usuarioService.gerarToken(usuario);

            return ResponseEntity.ok(new LoginResponseDTO(usuario, token));
        } catch (Exception e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> autenticar(@RequestBody LoginRequestDTO loginRequestDTO) {
        try {
            Usuario usuario = usuarioService.autenticarUsuario(loginRequestDTO.getEmail(), loginRequestDTO.getSenha());
            String token = usuarioService.gerarToken(usuario);
            return ResponseEntity.ok(new LoginResponseDTO(usuario, token));
        } catch (Exception e) {
            return ResponseEntity.status(401).body(e.getMessage());
        }
    }
}
