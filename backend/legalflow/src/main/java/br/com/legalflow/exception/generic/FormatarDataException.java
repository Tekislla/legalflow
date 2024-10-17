package br.com.legalflow.exception.generic;

public class FormatarDataException extends RuntimeException {

    public FormatarDataException(String data) {
        super("Não foi possível formatar a data: " + data);
    }
}
