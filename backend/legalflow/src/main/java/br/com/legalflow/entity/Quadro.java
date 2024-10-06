package br.com.legalflow.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table
public class Quadro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "organizacao_id")
    private Organizacao organizacao;
    @JsonManagedReference
    @ToString.Exclude
    @OneToMany(mappedBy = "quadro", cascade = CascadeType.ALL)
    private List<Processo> processos = new ArrayList<>();;
    private String nome;
}
