package br.com.nfse.nfse_saas.nfse.soap;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class NfseSoapClientTest {

    @Test
    void sanitizaRespostaDeErroEExtraiFaultString() {
        String resposta = """
                <soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/"><soap:Body>
                <soap:Fault><faultstring>RPS 1 ja foi utilizado</faultstring><senha>segredo</senha></soap:Fault>
                </soap:Body></soap:Envelope>
                """;

        String sanitizada = NfseSoapClient.sanitizarRespostaErro(resposta);

        assertThat(sanitizada).doesNotContain("segredo").contains("dado-sensivel");
        assertThat(NfseSoapClient.extrairDetalheErro(sanitizada)).isEqualTo("RPS 1 ja foi utilizado");
    }
}
