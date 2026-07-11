package br.com.nfse.nfse_saas.nfse.soap;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class NfseSoapClient {

    private static final String ENDPOINT =
            "https://nfse.issnetonline.com.br/abrasf204/goiania/nfse.asmx";

    public String enviar(
            String cabecalho,
            String xmlAssinado
    ) {

        try {

            /*
             * REMOVE XML DECLARATION
             */

            cabecalho =
                    removerDeclaracaoXml(cabecalho);

            xmlAssinado =
                    removerDeclaracaoXml(xmlAssinado);

            /*
             * SOAP XML
             */

            String soapXml =
                    montarSoap(
                            cabecalho,
                            xmlAssinado
                    );

            /*
             * LOG
             */
            System.out.println(">>> SOAP GERADO PELO MONTARSOAP SEM CDATA NO CABECALHO");

            if (soapXml.contains("<nfseCabecMsg><![CDATA[")) {
                throw new RuntimeException(
                        "ERRO: nfseCabecMsg ainda estÃ¡ saindo com CDATA. O mÃ©todo montarSoap usado na execuÃ§Ã£o nÃ£o Ã© o novo."
                );
            }
            salvarDebugXml(
                    "03-soap-final-enviado.xml",
                    soapXml
            );

            System.out.println("=================================");
            System.out.println("SOAP ENVIADO");
            System.out.println("=================================");

            System.out.println(soapXml);

            /*
             * CONEXAO
             */

            URL url =
                    new URL(ENDPOINT);

            HttpURLConnection connection =
                    (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("POST");

            connection.setDoOutput(true);

            connection.setRequestProperty(
                    "Content-Type",
                    "text/xml; charset=utf-8"
            );

            connection.setRequestProperty(
                    "SOAPAction",
                    "\"http://nfse.abrasf.org.br/GerarNfse\""
            );
            /*
             * ENVIA SOAP
             */

            try (OutputStream os =
                         connection.getOutputStream()) {

                os.write(
                        soapXml.getBytes(StandardCharsets.UTF_8)
                );
            }

            /*
             * STATUS
             */

            int status =
                    connection.getResponseCode();

            System.out.println("HTTP STATUS: " + status);

            /*
             * RESPOSTA
             */

            InputStream is;

            if (status >= 400) {

                is =
                        connection.getErrorStream();

            } else {

                is =
                        connection.getInputStream();
            }

            BufferedReader br =
                    new BufferedReader(
                            new InputStreamReader(
                                    is,
                                    StandardCharsets.UTF_8
                            )
                    );

            StringBuilder response =
                    new StringBuilder();

            String line;

            while ((line = br.readLine()) != null) {

                response.append(line);
            }
            salvarDebugXml(
                    "04-retorno-prefeitura.xml",
                    response.toString()
            );
            /*
             * LOG RESPONSE
             */

            System.out.println("=================================");
            System.out.println("SOAP RESPONSE");
            System.out.println("=================================");

            System.out.println(response);

            return response.toString();

        } catch (Exception e) {

            e.printStackTrace();

            return e.toString();
        }
    }

    /*
     * SOAP
     */
    private String montarSoap(String cabecalho, String xml) {
        return "<?xml version=\"1.0\" encoding=\"utf-8\"?>"
                + "<soap:Envelope "
                + "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" "
                + "xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" "
                + "xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">"
                + "<soap:Body>"
                + "<GerarNfse xmlns=\"http://nfse.abrasf.org.br\">"

                + "<nfseCabecMsg>"
                + cabecalho
                + "</nfseCabecMsg>"

                // TESTE AGORA SEM CDATA NO DADOS TAMBÃ‰M
                + "<nfseDadosMsg>"
                + xml
                + "</nfseDadosMsg>"

                + "</GerarNfse>"
                + "</soap:Body>"
                + "</soap:Envelope>";
    }
    /*
     * REMOVE XML DECLARATION
     */

    private String removerDeclaracaoXml(String xml) {
        if (xml == null) {
            return "";
        }

        return xml
                .replaceFirst("^\\s*<\\?xml[^>]*\\?>", "")
                .replace("\uFEFF", "")
                .trim();
    }
    
    
    private void salvarDebugXml(String nomeArquivo, String conteudo) {
        try {
            Path pasta = Paths.get("C:/temp/nfse-debug");

            Files.createDirectories(pasta);

            Path arquivo = pasta.resolve(nomeArquivo);

            Files.write(
                    arquivo,
                    conteudo.getBytes(StandardCharsets.UTF_8)
            );

            System.out.println("Arquivo debug salvo em: " + arquivo.toAbsolutePath());

        } catch (Exception e) {
            System.out.println("Erro ao salvar XML debug: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
