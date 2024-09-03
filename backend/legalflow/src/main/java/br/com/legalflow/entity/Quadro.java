package br.com.legalflow.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

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
    @OneToMany(mappedBy = "quadro", cascade = CascadeType.ALL)
    private List<Processo> processos;
    private String nome;

    @Override
    public String toString() {
        return "Quadro [id=" + id + ", nome=" + nome + "]";
    }
}
