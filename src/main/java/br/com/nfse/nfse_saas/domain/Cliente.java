package br.com.nfse.nfse_saas.domain;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    private String documento; // CPF ou CNPJ

    private String email;

    @ManyToOne
    @JoinColumn(name = "empresa_id")
    private Empresa empresa;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "endereco_id")
    private Endereco endereco;
}