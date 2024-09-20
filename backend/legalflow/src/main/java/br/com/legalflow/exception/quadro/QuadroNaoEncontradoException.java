package br.com.legalflow.exception.quadro;

public class QuadroNaoEncontradoException extends RuntimeException {

    public QuadroNaoEncontradoException(Long id) {
        super("Quadro com ID " + id + " não foi encontrado.");
    }
}
