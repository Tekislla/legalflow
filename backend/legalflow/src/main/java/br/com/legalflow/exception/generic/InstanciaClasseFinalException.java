package br.com.legalflow.exception.generic;

public class InstanciaClasseFinalException extends UnsupportedOperationException {

    public InstanciaClasseFinalException() {
        super("Essa classe serve apenas para utilidade, não deve ser instanciada");
    }
}
