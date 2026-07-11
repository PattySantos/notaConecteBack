package br.com.nfse.nfse_saas.nfse.dto;

import java.math.BigDecimal;

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
}
