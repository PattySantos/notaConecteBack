package br.com.nfse.nfse_saas.nfse.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NfseEmissaoResponseDTO {
    private Long notaId;
    private Long numeroNfse;
    private String status;
    private String retorno;
}