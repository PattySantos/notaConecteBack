package br.com.nfse.nfse_saas.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;


import lombok.*;

import java.util.List;

@Data
@Entity
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String email;
    private String senha;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("usuario")
    private List<Empresa> empresas;
}
