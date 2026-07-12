package br.com.nfse.nfse_saas.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Empresa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String cnpj;
    private String razaoSocial;
    private String email;
    private String telefone;

    @Column(columnDefinition = "TEXT")
    private String cnaes;

    private String logomarcaNome;

    @Column(columnDefinition = "TEXT")
    private String logomarcaBase64;

    private String certificadoDigitalNome;

    @Column(columnDefinition = "TEXT")
    private String certificadoDigitalBase64;

    private String senhaCertificadoDigital;

    private Long proximoNumeroNfse;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    @JsonIgnorgit status
    eProperties("empresas")
    private Usuario usuario;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "endereco_id")
    private Endereco endereco;
}
