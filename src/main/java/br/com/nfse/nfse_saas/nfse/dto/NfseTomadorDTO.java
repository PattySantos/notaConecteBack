package br.com.nfse.nfse_saas.nfse.dto;

import lombok.Data;

@Data
public class NfseTomadorDTO {
    private String cpfCnpjTomador;
    private String razaoSocialTomador;
    private String enderecoTomador;
    private String numero;
    private String bairroTomador;
    private String codigoMunicipioTomador = "5208707";
    private String ufTomador = "GO";
    private String cepTomador;
    private String telefoneTomador;
    private String emailTomador;
}
