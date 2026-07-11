package br.com.nfse.nfse_saas.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Entity
@Table(name = "servico_fiscal")
public class ServicoFiscal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20)
    private String codigoServicoNacional;

    @Column(nullable = false, length = 500)
    private String descricaoServicoNacional;

    @Column(length = 20)
    private String codigoNbs;

    @Column(length = 500)
    private String descricaoNbs;

    @Column(length = 50)
    private String indicadorIbscbs;

    @Column(length = 20)
    private String codigoServicoMunicipal;

    @Column(columnDefinition = "TEXT")
    private String cnaesRelacionados;

    private BigDecimal aliquotaIss;

    @Column(length = 7)
    private String municipioIbge;

    private Boolean ativo = true;
}
