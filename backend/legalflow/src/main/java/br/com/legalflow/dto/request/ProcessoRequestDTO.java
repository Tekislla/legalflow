package br.com.legalflow.dto.request;

import lombok.Data;

import java.util.Date;

@Data
public class ProcessoRequestDTO {

    private Long id;
    private Long quadroId;
    private String nome;
    private String numero;
    private String descricao;
    private Date prazoSubsidio;
    private Date prazoFatal;
    private byte[] arquivo;

}
