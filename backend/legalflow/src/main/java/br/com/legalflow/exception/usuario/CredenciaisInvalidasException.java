package br.com.legalflow.exception.usuario;

public class CredenciaisInvalidasException extends RuntimeException {

    public CredenciaisInvalidasException() {
        super("As credenciais fornecidas são inválidas");
    }
}
