package br.com.nfse.nfse_saas.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Entity
public class Empresa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String cnpj;
    private String razaoSocial;
    private String nomeFantasia;
    private String inscricaoMunicipal;
    private String inscricaoEstadual;
    private String email;
    private String telefone;
    private String situacaoCadastral;
    private String naturezaJuridica;
    private String cnaePrincipal;
    private String descricaoCnaePrincipal;
    private String regimeTributario;
    private String regimeEspecialTributacao;
    private Boolean optanteSimplesNacional;
    private Boolean incentivadorCultural;
    private Boolean incentivoFiscal;

    @Column(precision = 10, scale = 4)
    private BigDecimal aliquotaIss;

    private String itemListaServico;
    private String codigoTributacaoMunicipal;
    private String codigoTributacaoNacional;

    @Column(columnDefinition = "TEXT")
    private String descricaoServicoPadrao;

    private String ambienteEmissao;

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
    @JsonIgnoreProperties("empresas")
    private Usuario usuario;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "endereco_id")
    private Endereco endereco;
}
