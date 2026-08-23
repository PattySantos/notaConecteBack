package br.com.nfse.nfse_saas.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Base64;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

class CertificateValidatorTest {

    private final CertificateValidator validator = new CertificateValidator();

    @Test
    void rejeitaBase64InvalidoSemExporAExcecaoInterna() {
        assertThatThrownBy(() -> validator.validarA1("nao-e-base64", "senha"))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Certificado PFX/P12 invalido ou senha incorreta");
    }

    @Test
    void rejeitaArquivoAcimaDoLimite() {
        String oversized = Base64.getEncoder().encodeToString(new byte[10 * 1024 * 1024 + 1]);
        assertThatThrownBy(() -> validator.validarA1(oversized, "senha"))
                .isInstanceOf(ResponseStatusException.class)
                .satisfies(error -> assertThat(((ResponseStatusException) error).getStatusCode().value()).isEqualTo(400));
    }
}
