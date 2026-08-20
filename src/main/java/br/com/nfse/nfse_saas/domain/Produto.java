package br.com.nfse.nfse_saas.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Entity
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    private String codigo;

    private BigDecimal valor;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    private String codigoServicoNacional;

    private String codigoServicoMunicipal;

    private String codigoCnae;

    private String codigoNbs;

    private BigDecimal aliquotaIss;

    private Boolean issRetido = false;

    private Integer exigibilidadeIss = 1;

    private String municipioIncidenciaIbge = "5208707";

    private String indicadorIbscbs;

    private Boolean ativo = true;

    @ManyToOne
    @JoinColumn(name = "servico_fiscal_id")
    private ServicoFiscal servicoFiscal;

    @ManyToOne
    @JoinColumn(name = "nbs_fiscal_id")
    private ServicoFiscal nbsFiscal;

    @ManyToOne
    @JoinColumn(name = "ibscbs_fiscal_id")
    private ServicoFiscal ibscbsFiscal;

    @ManyToOne
    @JoinColumn(name = "empresa_id", nullable = false)
    @JsonIgnore
    private Empresa empresa;
}
