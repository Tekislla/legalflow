package br.com.legalflow.entity;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table
public class Organizacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String documento;
    @ToString.Exclude
    @JsonManagedReference
    @OneToMany(mappedBy = "organizacao", cascade = CascadeType.ALL)
    private List<Quadro> quadros = new ArrayList<>();
    @ToString.Exclude
    @JsonManagedReference
    @OneToMany(mappedBy = "organizacao", cascade = CascadeType.ALL)
    private List<Usuario> usuarios = new ArrayList<>();
}
