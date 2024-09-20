package br.com.legalflow.exception.usuario;

public class UsuarioInativoException extends RuntimeException {

    public UsuarioInativoException(String email) {
        super("Usuário com e-mail " + email + " está inativo.");
    }
}
