package br.com.nfse.nfse_saas.domain;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;

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

    private String status; // EMITIDA, CANCELADA

    @Column(columnDefinition = "TEXT")
    private String retornoEmissaoNfse;

    @ManyToOne
    @JoinColumn(name = "empresa_id")
    private Empresa empresa;
}
