package br.com.legalflow.dto.request;

import lombok.Data;

@Data
public class EditarUsuarioRequestDTO {

    private Long id;
    private String nome;
    private String senha;
    private String novaSenha;
    private boolean ativo;
    private boolean administrador;
}
