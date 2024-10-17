package br.com.legalflow.exception.organizacao;

public class OrganizacaoJaCadastradaException extends RuntimeException {

    public OrganizacaoJaCadastradaException(String documento) {
        super("Organização com documento " + documento + " já cadastrada");
    }
}
