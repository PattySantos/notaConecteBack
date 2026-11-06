package br.com.nfse.nfse_saas.nfse.soap;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class NfseSoapClient {

    private static final String SOAP_ACTION = "http://nfse.abrasf.org.br/GerarNfse";
    private static final int MAX_ERROR_RESPONSE_LENGTH = 12_000;
    private static final Pattern MENSAGEM_ERRO = Pattern.compile(
            "(?is)<(?:\\w+:)?(?:faultstring|mensagem|message)[^>]*>(.*?)</(?:\\w+:)?(?:faultstring|mensagem|message)>"
    );
    private static final Pattern TAG_SENSIVEL = Pattern.compile(
            "(?is)<(?:\\w+:)?(?:senha|password|certificado|certificate)[^>]*>.*?</(?:\\w+:)?(?:senha|password|certificado|certificate)>"
    );

    private final HttpClient httpClient;
    private final String endpointProducao;
    private final String endpointHomologacao;

    public NfseSoapClient(
            @Value("${nfse.goiania.endpoint-producao}") String endpointProducao,
            @Value("${nfse.goiania.endpoint-homologacao:}") String endpointHomologacao,
            @Value("${nfse.http.connect-timeout-seconds:10}") long connectTimeoutSeconds
    ) {
        this.endpointProducao = endpointProducao;
        this.endpointHomologacao = endpointHomologacao;
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(connectTimeoutSeconds))
                .build();
    }

    public String enviar(String ambiente, String cabecalho, String xmlAssinado) throws IOException, InterruptedException {
        String endpoint = selecionarEndpoint(ambiente);
        String soapXml = montarSoap(removerDeclaracaoXml(cabecalho), removerDeclaracaoXml(xmlAssinado));

        HttpRequest request = HttpRequest.newBuilder(URI.create(endpoint))
                .timeout(Duration.ofSeconds(30))
                .header("Content-Type", "text/xml; charset=utf-8")
                .header("SOAPAction", '"' + SOAP_ACTION + '"')
                .POST(HttpRequest.BodyPublishers.ofString(soapXml, StandardCharsets.UTF_8))
                .build();

        HttpResponse<String> response = httpClient.send(
                request,
                HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8)
        );

        if (response.statusCode() < 200 || response.statusCode() >= 300) {
            String respostaSanitizada = sanitizarRespostaErro(response.body());
            throw new NfseSoapException(
                    response.statusCode(),
                    extrairDetalheErro(respostaSanitizada),
                    respostaSanitizada
            );
        }
        if (response.body() == null || response.body().isBlank()) {
            throw new IOException("SGISS retornou uma resposta vazia");
        }
        return response.body();
    }

    private String selecionarEndpoint(String ambiente) {
        if ("PRODUCAO".equalsIgnoreCase(ambiente)) {
            return endpointProducao;
        }
        if (endpointHomologacao == null || endpointHomologacao.isBlank()) {
            throw new IllegalStateException(
                    "Ambiente de homologacao nao configurado. Defina NFSE_GOIANIA_ENDPOINT_HOMOLOGACAO ou selecione PRODUCAO."
            );
        }
        return endpointHomologacao;
    }

    String montarSoap(String cabecalho, String xml) {
        return "<?xml version=\"1.0\" encoding=\"utf-8\"?>"
                + "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" "
                + "xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" "
                + "xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">"
                + "<soap:Body><GerarNfse xmlns=\"http://nfse.abrasf.org.br\">"
                + "<nfseCabecMsg>" + cabecalho + "</nfseCabecMsg>"
                + "<nfseDadosMsg>" + xml + "</nfseDadosMsg>"
                + "</GerarNfse></soap:Body></soap:Envelope>";
    }

    private String removerDeclaracaoXml(String xml) {
        if (xml == null) return "";
        return xml.replaceFirst("^\\s*<\\?xml[^>]*\\?>", "").replace("\uFEFF", "").trim();
    }

    static String sanitizarRespostaErro(String resposta) {
        if (resposta == null || resposta.isBlank()) return "";
        String sanitizada = TAG_SENSIVEL.matcher(resposta).replaceAll("<dado-sensivel>***</dado-sensivel>");
        return sanitizada.length() <= MAX_ERROR_RESPONSE_LENGTH
                ? sanitizada
                : sanitizada.substring(0, MAX_ERROR_RESPONSE_LENGTH) + "... [resposta truncada]";
    }

    static String extrairDetalheErro(String resposta) {
        if (resposta == null || resposta.isBlank()) return "";
        Matcher matcher = MENSAGEM_ERRO.matcher(resposta);
        String detalhe = matcher.find() ? matcher.group(1) : resposta;
        detalhe = detalhe.replaceAll("(?is)<[^>]+>", " ")
                .replace("&lt;", "<")
                .replace("&gt;", ">")
                .replace("&amp;", "&")
                .replaceAll("\\s+", " ")
                .trim();
        return detalhe.length() <= 1_000 ? detalhe : detalhe.substring(0, 1_000) + "...";
    }
}
