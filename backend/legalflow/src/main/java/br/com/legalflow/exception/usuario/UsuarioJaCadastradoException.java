package br.com.legalflow.exception.usuario;

public class UsuarioJaCadastradoException extends RuntimeException {

    public UsuarioJaCadastradoException(String email) {
        super("Já existe um usuário cadastrado com o e-mail " + email);
    }
}
