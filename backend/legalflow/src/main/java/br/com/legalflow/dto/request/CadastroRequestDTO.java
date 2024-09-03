package br.com.legalflow.dto.request;

import lombok.Data;

@Data
public class CadastroRequestDTO {

    private String nome;
    private String email;
    private String senha;
    private Long organizacaoId;
    private String nomeOrganizacao;
    private String documentoOrganizacao;
    private boolean administrador;
}
