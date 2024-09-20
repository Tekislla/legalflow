package br.com.legalflow.exception.usuario;

public class UsuarioNaoAutorizadoException extends RuntimeException {

    public UsuarioNaoAutorizadoException(String email, String acao) {
        super("Usuário com e-mail " + email + " não autorizado a realizar a ação: " + acao);
    }
}
