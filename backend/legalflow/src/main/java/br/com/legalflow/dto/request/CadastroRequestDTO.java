package br.com.legalflow.dto.request;

import br.com.legalflow.entity.Organizacao;
import lombok.Data;

@Data
public class CadastroRequestDTO {

    private String nome;
    private String email;
    private String senha;
    private String nomeOrganizacao;
    private String documentoOrganizacao;
    private Organizacao organizacao;
    private boolean administrador;
}
