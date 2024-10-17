package br.com.legalflow.exception.organizacao;

public class OrganizacaoNaoEncontradaException extends RuntimeException {

    public OrganizacaoNaoEncontradaException(Long id) {
        super("Organização com id " + id + " não foi encontrada");
    }
}
