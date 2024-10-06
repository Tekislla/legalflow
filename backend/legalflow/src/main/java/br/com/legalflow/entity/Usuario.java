package br.com.legalflow.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "organizacao_id")
    private Organizacao organizacao;
    private String nome;
    private String email;
    private String senhaCrypto;
    private Date dataCadastro;
    private Date dataAtualizacao;
    private boolean ativo;
    private String role;
    @ToString.Exclude
    @JsonManagedReference
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    private List<Quadro> quadros = new ArrayList<>();
}
