package br.com.nfse.nfse_saas.nfse.service;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilderFactory;

import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

@Component
public class NfseResponseParser {

    public NfseMunicipalResponse parse(String soapResponse) {
        Document soap = parseXml(soapResponse);
        String fault = firstText(soap, "faultstring");
        if (!fault.isBlank()) return rejeitada(fault);

        String municipalXml = localizarXmlMunicipal(soap);
        Document municipal = parseXml(municipalXml);
        Element infNfse = firstElement(municipal, "InfNfse");
        if (infNfse != null) {
            String numero = firstText(infNfse, "Numero");
            if (numero.isBlank()) throw new IllegalArgumentException("Resposta autorizada sem numero da NFS-e");
            return new NfseMunicipalResponse(
                    true,
                    Long.valueOf(numero.replaceAll("\\D", "")),
                    firstText(infNfse, "CodigoVerificacao"),
                    municipalXml,
                    "NFS-e autorizada pelo SGISS"
            );
        }

        List<String> mensagens = new ArrayList<>();
        NodeList nodes = municipal.getElementsByTagNameNS("*", "MensagemRetorno");
        for (int i = 0; i < nodes.getLength(); i++) {
            Element item = (Element) nodes.item(i);
            String codigo = firstText(item, "Codigo");
            String mensagem = firstText(item, "Mensagem");
            String correcao = firstText(item, "Correcao");
            mensagens.add(String.join(" - ", partes(codigo, mensagem, correcao)));
        }
        if (mensagens.isEmpty()) mensagens.add("SGISS nao retornou uma NFS-e autorizada");
        return rejeitada(String.join("; ", mensagens));
    }

    private String localizarXmlMunicipal(Document soap) {
        NodeList results = soap.getElementsByTagNameNS("*", "GerarNfseResult");
        if (results.getLength() == 0) results = soap.getElementsByTagNameNS("*", "outputXML");
        if (results.getLength() == 0) {
            Element respostaDireta = firstElement(soap, "GerarNfseResposta");
            if (respostaDireta != null) return toXml(respostaDireta);
            throw new IllegalArgumentException("Resposta SOAP sem GerarNfseResult: " + descreverElementos(soap));
        }

        Node result = results.item(0);
        String text = result.getTextContent() == null ? "" : result.getTextContent().trim();
        if (text.startsWith("<")) return text;
        for (Node child = result.getFirstChild(); child != null; child = child.getNextSibling()) {
            if (child instanceof Element element) return toXml(element);
        }
        throw new IllegalArgumentException("Resposta SOAP sem XML municipal");
    }

    private String descreverElementos(Document document) {
        NodeList elementos = document.getElementsByTagNameNS("*", "*");
        List<String> nomes = new ArrayList<>();
        for (int i = 0; i < elementos.getLength() && nomes.size() < 8; i++) {
            String nome = elementos.item(i).getLocalName();
            if (nome != null && !nomes.contains(nome)) nomes.add(nome);
        }
        return nomes.isEmpty() ? "envelope sem elementos reconheciveis" : String.join(", ", nomes);
    }

    private NfseMunicipalResponse rejeitada(String mensagem) {
        return new NfseMunicipalResponse(false, null, null, null, mensagem);
    }

    private Document parseXml(String xml) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            factory.setFeature("http://xml.org/sax/features/external-general-entities", false);
            factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
            return factory.newDocumentBuilder().parse(new InputSource(new StringReader(xml)));
        } catch (Exception e) {
            throw new IllegalArgumentException("Resposta XML invalida do SGISS", e);
        }
    }

    private Element firstElement(Node node, String localName) {
        NodeList nodes = node instanceof Document document
                ? document.getElementsByTagNameNS("*", localName)
                : ((Element) node).getElementsByTagNameNS("*", localName);
        return nodes.getLength() == 0 ? null : (Element) nodes.item(0);
    }

    private String firstText(Node node, String localName) {
        Element element = firstElement(node, localName);
        return element == null || element.getTextContent() == null ? "" : element.getTextContent().trim();
    }

    private List<String> partes(String... valores) {
        List<String> partes = new ArrayList<>();
        for (String valor : valores) if (valor != null && !valor.isBlank()) partes.add(valor);
        return partes;
    }

    private String toXml(Node node) {
        try {
            var factory = javax.xml.transform.TransformerFactory.newInstance();
            factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            var transformer = factory.newTransformer();
            transformer.setOutputProperty(javax.xml.transform.OutputKeys.OMIT_XML_DECLARATION, "yes");
            var writer = new java.io.StringWriter();
            transformer.transform(new javax.xml.transform.dom.DOMSource(node), new javax.xml.transform.stream.StreamResult(writer));
            return writer.toString();
        } catch (Exception e) {
            throw new IllegalArgumentException("Nao foi possivel ler o XML municipal", e);
        }
    }
}
