package br.com.nfse.nfse_saas.nfse.dto;

import lombok.Data;

@Data
public class NfsePrestadorDTO {
    private String cpfCnpjPrestador;
    private String inscricaoMunicipalPrestador;
    private String codigoCnae;
    private String codigoTributacaoMunicipio;
}
