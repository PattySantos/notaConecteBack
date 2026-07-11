package br.com.nfse.nfse_saas.nfse.service;

import lombok.Data;

@Data
public class NfseResumoPdfDTO {
    private String numeroNfse;
    private String codigoVerificacao;
    private String dataEmissao;
    private String competencia;
    private String discriminacao;
    private String valorServicos;
    private String valorIss;
}
