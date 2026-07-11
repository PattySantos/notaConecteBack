package br.com.nfse.nfse_saas.nfse.xml;

public class ConsultarUrlNfseXmlBuilder {

    public String gerarXmlPorNumeroNfse(
            String cnpjPrestador,
            String inscricaoMunicipal,
            String numeroNfse
    ) {
        StringBuilder xml = new StringBuilder();

        xml.append("<ConsultarUrlNfseEnvio xmlns=\"http://www.abrasf.org.br/nfse.xsd\">");

        xml.append("<Pedido>");

        xml.append("<Prestador>");
        xml.append("<CpfCnpj>");
        xml.append("<Cnpj>").append(somenteNumeros(cnpjPrestador)).append("</Cnpj>");
        xml.append("</CpfCnpj>");
        xml.append("<InscricaoMunicipal>").append(somenteNumeros(inscricaoMunicipal)).append("</InscricaoMunicipal>");
        xml.append("</Prestador>");

        xml.append("<NumeroNfse>").append(somenteNumeros(numeroNfse)).append("</NumeroNfse>");

        xml.append("<Pagina>1</Pagina>");

        xml.append("</Pedido>");

        xml.append("</ConsultarUrlNfseEnvio>");

        return xml.toString();
    }

    public String gerarXmlPorRps(
            String cnpjPrestador,
            String inscricaoMunicipal,
            String numeroRps,
            String serieRps,
            String tipoRps
    ) {
        StringBuilder xml = new StringBuilder();

        xml.append("<ConsultarUrlNfseEnvio xmlns=\"http://www.abrasf.org.br/nfse.xsd\">");

        xml.append("<Pedido>");

        xml.append("<Prestador>");
        xml.append("<CpfCnpj>");
        xml.append("<Cnpj>").append(somenteNumeros(cnpjPrestador)).append("</Cnpj>");
        xml.append("</CpfCnpj>");
        xml.append("<InscricaoMunicipal>").append(somenteNumeros(inscricaoMunicipal)).append("</InscricaoMunicipal>");
        xml.append("</Prestador>");

        xml.append("<IdentificacaoRps>");
        xml.append("<Numero>").append(somenteNumeros(numeroRps)).append("</Numero>");
        xml.append("<Serie>").append(somenteNumeros(serieRps)).append("</Serie>");
        xml.append("<Tipo>").append(somenteNumeros(tipoRps)).append("</Tipo>");
        xml.append("</IdentificacaoRps>");

        xml.append("<Pagina>1</Pagina>");

        xml.append("</Pedido>");

        xml.append("</ConsultarUrlNfseEnvio>");

        return xml.toString();
    }

    private String somenteNumeros(String valor) {
        if (valor == null) {
            return "";
        }

        return valor.replaceAll("\\D", "");
    }
}
