package br.com.legalflow.dto.request;

import lombok.Data;

@Data
public class QuadroRequestDTO {

    private Long id;
    private String nome;
    private String numero;
    private Long usuarioId;
    private Long organizacaoId;

}
