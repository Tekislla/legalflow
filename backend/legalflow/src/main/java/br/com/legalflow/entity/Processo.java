package br.com.legalflow.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Arrays;
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
    private String descricao;
    private String status;
    private Date prazoSubsidio;
    private Date prazoFatal;
    private byte[] arquivo;

    @Override
    public String toString() {
        return "Processo [id=" + id + ", nome=" + nome + ", numero=" + numero + ", descricao=" + descricao + ", status=" + status
                + ", prazoSubsidio=" + prazoSubsidio + ", prazoFatal=" + prazoFatal + "]";
    }
}
