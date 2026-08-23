package br.com.nfse.nfse_saas.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import br.com.nfse.nfse_saas.security.EncryptedStringConverter;

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
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Convert(converter = EncryptedStringConverter.class)
    private String certificadoDigitalBase64;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Convert(converter = EncryptedStringConverter.class)
    private String senhaCertificadoDigital;

    private Long proximoNumeroNfse;

    private Long proximoNumeroRps;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public boolean isCertificadoDigitalConfigurado() {
        return certificadoDigitalBase64 != null && !certificadoDigitalBase64.isBlank()
                && senhaCertificadoDigital != null && !senhaCertificadoDigital.isBlank();
    }

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    @JsonIgnoreProperties("empresas")
    private Usuario usuario;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "endereco_id")
    private Endereco endereco;
}
