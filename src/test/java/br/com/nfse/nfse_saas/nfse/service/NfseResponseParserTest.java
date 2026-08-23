package br.com.nfse.nfse_saas.nfse.service;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;

class NfseResponseParserTest {
    private final NfseResponseParser parser = new NfseResponseParser();

    @Test
    void reconheceNfseAutorizadaDentroDoSoap() {
        String soap = """
                <soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/"><soap:Body>
                <GerarNfseResponse xmlns="http://nfse.abrasf.org.br"><GerarNfseResult>
                &lt;GerarNfseResposta xmlns=&quot;http://www.abrasf.org.br/nfse.xsd&quot;&gt;&lt;ListaNfse&gt;&lt;CompNfse&gt;&lt;Nfse&gt;&lt;InfNfse&gt;&lt;Numero&gt;42&lt;/Numero&gt;&lt;CodigoVerificacao&gt;ABC123&lt;/CodigoVerificacao&gt;&lt;/InfNfse&gt;&lt;/Nfse&gt;&lt;/CompNfse&gt;&lt;/ListaNfse&gt;&lt;/GerarNfseResposta&gt;
                </GerarNfseResult></GerarNfseResponse></soap:Body></soap:Envelope>
                """;
        NfseMunicipalResponse resposta = parser.parse(soap);
        assertThat(resposta.autorizada()).isTrue();
        assertThat(resposta.numeroNfse()).isEqualTo(42L);
        assertThat(resposta.codigoVerificacao()).isEqualTo("ABC123");
    }

    @Test
    void preservaMensagemDeRejeicaoMunicipal() {
        String soap = """
                <soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/"><soap:Body>
                <GerarNfseResponse xmlns="http://nfse.abrasf.org.br"><GerarNfseResult>
                &lt;GerarNfseResposta xmlns=&quot;http://www.abrasf.org.br/nfse.xsd&quot;&gt;&lt;ListaMensagemRetorno&gt;&lt;MensagemRetorno&gt;&lt;Codigo&gt;E10&lt;/Codigo&gt;&lt;Mensagem&gt;RPS ja informado&lt;/Mensagem&gt;&lt;Correcao&gt;Use outro numero&lt;/Correcao&gt;&lt;/MensagemRetorno&gt;&lt;/ListaMensagemRetorno&gt;&lt;/GerarNfseResposta&gt;
                </GerarNfseResult></GerarNfseResponse></soap:Body></soap:Envelope>
                """;
        NfseMunicipalResponse resposta = parser.parse(soap);
        assertThat(resposta.autorizada()).isFalse();
        assertThat(resposta.mensagem()).contains("E10", "RPS ja informado", "Use outro numero");
    }

    @Test
    void reconheceSoapFaultComoRejeicao() {
        String soap = """
                <soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/"><soap:Body>
                <soap:Fault><faultcode>soap:Server</faultcode><faultstring>Falha municipal</faultstring></soap:Fault>
                </soap:Body></soap:Envelope>
                """;
        assertThat(parser.parse(soap).mensagem()).isEqualTo("Falha municipal");
    }
}
