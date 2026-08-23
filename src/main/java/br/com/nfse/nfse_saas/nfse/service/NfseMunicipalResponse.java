package br.com.nfse.nfse_saas.nfse.service;

public record NfseMunicipalResponse(
        boolean autorizada,
        Long numeroNfse,
        String codigoVerificacao,
        String xmlNfse,
        String mensagem
) {
}
