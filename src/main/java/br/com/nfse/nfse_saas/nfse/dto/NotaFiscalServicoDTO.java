package br.com.nfse.nfse_saas.nfse.dto;

import lombok.Data;

@Data
public class NotaFiscalServicoDTO {
    private NfseNotaDTO notaFiscal = new NfseNotaDTO();
    private NfsePrestadorDTO prestador = new NfsePrestadorDTO();
    private NfseTomadorDTO tomador = new NfseTomadorDTO();
}
