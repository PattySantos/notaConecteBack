package br.com.nfse.nfse_saas.domain;

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
    private Empresa empresa;
}
