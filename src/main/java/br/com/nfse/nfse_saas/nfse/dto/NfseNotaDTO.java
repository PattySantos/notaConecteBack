package br.com.nfse.nfse_saas.nfse.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Data;

@Data
public class NfseNotaDTO {
    private String numeroRps;
    private String serieRps = "1";
    private String tipoRps = "1";
    private BigDecimal valorServicos = BigDecimal.ZERO;
    private BigDecimal aliquotaIss = BigDecimal.ZERO;
    private String issRetido = "2";
    private String codigoItemListaServico;
    private String codigoNbs;
    private String discriminacaoServico;
    private LocalDate dataCompetencia;
    private String codigoCnae;
    private String codigoTributacaoMunicipio;
    private String codigoMunicipio = "5208707";
    private Integer exigibilidadeIss = 1;
    private BigDecimal valorDeducoes = BigDecimal.ZERO;
    private BigDecimal descontoIncondicionado = BigDecimal.ZERO;
    private BigDecimal descontoCondicionado = BigDecimal.ZERO;
    private BigDecimal valorPis = BigDecimal.ZERO;
    private BigDecimal valorCofins = BigDecimal.ZERO;
    private BigDecimal valorInss = BigDecimal.ZERO;
    private BigDecimal valorIr = BigDecimal.ZERO;
    private BigDecimal valorCsll = BigDecimal.ZERO;
    private BigDecimal outrasRetencoes = BigDecimal.ZERO;
    private String processoSuspensao;
    private String cno;
    private String art;
    private String informacoesComplementares;
}
