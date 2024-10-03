package br.com.legalflow.dto.request;

import lombok.Data;

@Data
public class ProcessoRequestDTO {

    private Long id;
    private Long quadroId;
    private String nome;
    private String numero;
    private String status;
    private String autor;
    private String reu;
    private String descricao;
    private String prazoSubsidio;
    private String prazoFatal;

}
