package br.com.legalflow.exception.processo;

public class ProcessoNaoEncontradoException extends RuntimeException {

    public ProcessoNaoEncontradoException(Long id) {
        super("Processo com ID " + id + " não foi encontrado");
    }
}
