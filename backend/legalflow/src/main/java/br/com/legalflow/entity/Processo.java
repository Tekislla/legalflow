package br.com.legalflow.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
@Entity
@Table
public class Processo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "quadro_id")
    private Quadro quadro;
    private String nome;
    private String numero;
    private String autor;
    private String reu;
    private String descricao;
    private String status;
    private Date prazoSubsidio;
    private Date prazoFatal;
    @JsonIgnore
    private byte[] arquivo;
}
