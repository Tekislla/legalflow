package br.com.legalflow.controller.base;

import br.com.legalflow.entity.Usuario;
import br.com.legalflow.enums.RoleEnum;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

public abstract class BaseController {

    protected Usuario getUsuarioLogado() {
        SecurityContext context = SecurityContextHolder.getContext();
        if (context != null && context.getAuthentication() != null) {
            Object principal = context.getAuthentication().getPrincipal();
            if (principal instanceof Usuario usuario) {
                return usuario;
            }
        }
        throw new IllegalStateException("Usuário não autenticado");
    }

    protected boolean isUsuarioAdmin() {
        Usuario usuario = getUsuarioLogado();
        return usuario.getRole().equals(RoleEnum.ADMIN.toString());
    }
}
