package br.com.legalflow.controller;

import br.com.legalflow.dto.request.LoginRequestDTO;
import br.com.legalflow.dto.response.LoginResponseDTO;
import br.com.legalflow.entity.Usuario;
import br.com.legalflow.service.UsuarioService;
import br.com.legalflow.utils.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @PostMapping("/login")
    public ResponseEntity<?> autenticar(@RequestBody LoginRequestDTO loginRequestDTO) {
        try {
            Usuario usuario = usuarioService.autenticarUsuario(loginRequestDTO.getEmail(), loginRequestDTO.getSenha());
            String token = jwtTokenProvider.gerarToken(usuario);
            return ResponseEntity.ok(new LoginResponseDTO(usuario, token));
        } catch (Exception e) {
            return ResponseEntity.status(401).body(e.getMessage());
        }
    }
}
