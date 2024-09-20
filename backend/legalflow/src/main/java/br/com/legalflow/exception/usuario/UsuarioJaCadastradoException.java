package br.com.legalflow.exception.usuario;

public class UsuarioJaCadastradoException extends RuntimeException {

    public UsuarioJaCadastradoException(String email) {
        super("Usuário com e-mail " + email + " já está cadastrado.");
    }
}
