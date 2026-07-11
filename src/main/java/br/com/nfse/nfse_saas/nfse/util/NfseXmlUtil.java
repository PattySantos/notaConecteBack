package br.com.nfse.nfse_saas.nfse.util;


import java.io.StringReader;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class NfseXmlUtil {

	public static String extrairTagDentroDe(String xml, String nomeTagPai, String nomeTagFilha) {
	    try {
	        Document document = parseXml(xml);

	        NodeList listaPai = document.getElementsByTagNameNS(
	                "*",
	                nomeTagPai
	        );

	        if (listaPai == null || listaPai.getLength() == 0) {
	            return null;
	        }

	        Element elementoPai = (Element) listaPai.item(0);

	        NodeList filhos = elementoPai.getChildNodes();

	        for (int i = 0; i < filhos.getLength(); i++) {
	            Node node = filhos.item(i);

	            if (node.getNodeType() == Node.ELEMENT_NODE) {
	                String localName = node.getLocalName();

	                if (nomeTagFilha.equals(localName)) {
	                    return node.getTextContent().trim();
	                }
	            }
	        }

	        return null;

	    } catch (Exception e) {
	        throw new RuntimeException(
	                "Erro ao extrair tag " + nomeTagFilha + " dentro de " + nomeTagPai,
	                e
	        );
	    }
	}

    public static Document parseXml(String xml) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        factory.setNamespaceAware(true);

        factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
        factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
        factory.setFeature("http://xml.org/sax/features/external-general-entities", false);
        factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);

        DocumentBuilder builder = factory.newDocumentBuilder();

        return builder.parse(
                new InputSource(
                        new StringReader(xml)
                )
        );
    }
}

