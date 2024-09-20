package br.com.legalflow.exception.organizacao;

public class OrganizacaoNaoEncontradaException extends RuntimeException {

    public OrganizacaoNaoEncontradaException(String documento) {
        super("Organização com documento " + documento + " não foi encontrada.");
    }

    public OrganizacaoNaoEncontradaException(Long id) {
        super("Organização com id " + id + " não foi encontrada.");
    }
}
