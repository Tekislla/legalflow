package br.com.legalflow.controller;

import br.com.legalflow.controller.base.BaseController;
import br.com.legalflow.entity.Usuario;
import br.com.legalflow.enums.RoleEnum;
import br.com.legalflow.exception.usuario.UsuarioNaoAutorizadoException;
import br.com.legalflow.service.OrganizacaoService;
import br.com.legalflow.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/usuario")
public class UsuarioController extends BaseController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/")
    public ResponseEntity<?> getUsuarioInfo() {
        try {
            Usuario usuarioLogado = getUsuarioLogado();
            List<Usuario> usuarios = new ArrayList<>();

            if (usuarioLogado.getRole().equals(RoleEnum.ADMIN.toString())) {
                usuarios = usuarioService.findByOrganizacaoId(usuarioLogado.getOrganizacao().getId());
            } else {
                usuarios.add(usuarioService.findById(usuarioLogado.getId()));
            }

            return ResponseEntity.ok(usuarios);
        } catch (Exception e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletarUsuario(@PathVariable Long id) {
        try {
            Usuario usuarioLogado = getUsuarioLogado();

            if (usuarioLogado.getRole().equals(RoleEnum.USER.toString())) {
                throw new UsuarioNaoAutorizadoException();
            }

            Usuario usuario = usuarioService.findById(id);
            usuarioService.excluirUsuario(usuario);

            return ResponseEntity.ok("Usuário excluído com sucesso");
        } catch (Exception e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

}
