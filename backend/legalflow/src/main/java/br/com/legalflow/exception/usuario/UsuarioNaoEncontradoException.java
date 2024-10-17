package br.com.legalflow.exception.usuario;

public class UsuarioNaoEncontradoException extends RuntimeException {

    public UsuarioNaoEncontradoException(String email) {
        super("Usuário com e-mail " + email + " não foi encontrado");
    }

    public UsuarioNaoEncontradoException(Long id) {
        super("Usuário com ID " + id + " não foi encontrado");
    }
}
