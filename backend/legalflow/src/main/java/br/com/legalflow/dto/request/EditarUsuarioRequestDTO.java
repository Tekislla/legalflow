package br.com.legalflow.dto.request;

import lombok.Data;

@Data
public class EditarUsuarioRequestDTO {

    private Long id;
    private String senha;
    private boolean ativo;
    private boolean administrador;
}
