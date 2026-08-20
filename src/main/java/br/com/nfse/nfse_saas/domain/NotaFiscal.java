package br.com.nfse.nfse_saas.domain;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Entity
public class NotaFiscal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long numeroNfse;

    private Long clienteId;

    private String descricao;

    private BigDecimal valor;

    private BigDecimal iss;

    private String codigoServico;

    private String codigoServicoNacional;

    private String codigoNbs;

    private String indicadorIbscbs;

    private String municipio;

    private Long produtoId;
    private LocalDate dataCompetencia;
    private String numeroRps;
    private String serieRps;
    private Integer tipoRps;
    private String localPrestacao;
    private String paisPrestacao;
    private String codigoCnae;
    private Integer exigibilidadeIss;
    private Boolean issRetido = false;
    private BigDecimal valorDeducoes;
    private BigDecimal descontoIncondicionado;
    private BigDecimal descontoCondicionado;
    private BigDecimal valorPis;
    private BigDecimal valorCofins;
    private BigDecimal valorInss;
    private BigDecimal valorIr;
    private BigDecimal valorCsll;
    private BigDecimal outrasRetencoes;
    private String processoSuspensao;
    private String cno;
    private String art;

    @Column(columnDefinition = "TEXT")
    private String informacoesComplementares;

    private String status; // EMITIDA, CANCELADA

    @Column(columnDefinition = "TEXT")
    private String retornoEmissaoNfse;

    @ManyToOne
    @JoinColumn(name = "empresa_id")
    private Empresa empresa;
}
