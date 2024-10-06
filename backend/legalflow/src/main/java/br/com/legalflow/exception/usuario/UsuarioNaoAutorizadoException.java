package br.com.legalflow.exception.usuario;

public class UsuarioNaoAutorizadoException extends RuntimeException {

    public UsuarioNaoAutorizadoException() {
        super("Você não tem permissão para realizar esta ação");
    }
}
