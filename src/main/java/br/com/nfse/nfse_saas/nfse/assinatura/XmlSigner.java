package br.com.nfse.nfse_saas.nfse.assinatura;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;

import javax.xml.XMLConstants;
import javax.xml.crypto.dsig.CanonicalizationMethod;
import javax.xml.crypto.dsig.DigestMethod;
import javax.xml.crypto.dsig.Reference;
import javax.xml.crypto.dsig.SignatureMethod;
import javax.xml.crypto.dsig.SignedInfo;
import javax.xml.crypto.dsig.Transform;
import javax.xml.crypto.dsig.XMLSignature;
import javax.xml.crypto.dsig.XMLSignatureFactory;
import javax.xml.crypto.dsig.dom.DOMSignContext;
import javax.xml.crypto.dsig.keyinfo.KeyInfo;
import javax.xml.crypto.dsig.keyinfo.KeyInfoFactory;
import javax.xml.crypto.dsig.keyinfo.X509Data;
import javax.xml.crypto.dsig.spec.C14NMethodParameterSpec;
import javax.xml.crypto.dsig.spec.TransformParameterSpec;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

public class XmlSigner {

    public String assinar(
            String xml,
            String tag,
            String caminhoCertificado,
            String senha
    ) throws Exception {

        System.out.println(">>> XmlSigner Nota Control - URI com Id, 2 transforms");

        validarParametros(xml, tag, caminhoCertificado, senha);

        xml = xml
                .replaceAll(">\\s+<", "><")
                .trim();

        Document document = carregarXml(xml);

        Node node = document
                .getElementsByTagNameNS("*", tag)
                .item(0);

        if (node == null) {
            throw new IllegalArgumentException("Tag nÃ£o encontrada para assinatura: " + tag);
        }

        Element element = (Element) node;

        if (!element.hasAttribute("Id")) {
            throw new IllegalArgumentException("Elemento " + tag + " nÃ£o possui atributo Id.");
        }

        String id = element.getAttribute("Id");

        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Atributo Id vazio no elemento " + tag);
        }

        element.setIdAttribute("Id", true);

        char[] senhaArray = senha.toCharArray();

        KeyStore keyStore = carregarCertificado(caminhoCertificado, senhaArray);

        String alias = buscarAliasComChavePrivada(keyStore);

        if (alias == null) {
            throw new IllegalArgumentException("Nenhum alias com chave privada encontrado no certificado.");
        }

        PrivateKey privateKey = (PrivateKey) keyStore.getKey(alias, senhaArray);

        if (privateKey == null) {
            throw new IllegalArgumentException("NÃ£o foi possÃ­vel carregar a chave privada do certificado.");
        }

        X509Certificate certificado = (X509Certificate) keyStore.getCertificate(alias);

        if (certificado == null) {
            throw new IllegalArgumentException("NÃ£o foi possÃ­vel carregar o certificado X509.");
        }

        XMLSignatureFactory signatureFactory = XMLSignatureFactory.getInstance("DOM");

        Transform envelopedTransform = signatureFactory.newTransform(
                Transform.ENVELOPED,
                (TransformParameterSpec) null
        );

        Transform c14nTransform = signatureFactory.newTransform(
                CanonicalizationMethod.INCLUSIVE,
                (TransformParameterSpec) null
        );

        Reference reference = signatureFactory.newReference(
                "#" + id,
                signatureFactory.newDigestMethod(DigestMethod.SHA1, null),
                Arrays.asList(envelopedTransform, c14nTransform),
                null,
                null
        );

        SignedInfo signedInfo = signatureFactory.newSignedInfo(
                signatureFactory.newCanonicalizationMethod(
                        CanonicalizationMethod.INCLUSIVE,
                        (C14NMethodParameterSpec) null
                ),
                signatureFactory.newSignatureMethod(
                        SignatureMethod.RSA_SHA1,
                        null
                ),
                Collections.singletonList(reference)
        );

        KeyInfoFactory keyInfoFactory = signatureFactory.getKeyInfoFactory();

        X509Data x509Data = keyInfoFactory.newX509Data(
                Collections.singletonList(certificado)
        );

        KeyInfo keyInfo = keyInfoFactory.newKeyInfo(
                Collections.singletonList(x509Data)
        );

        Node parentNode = node.getParentNode();

        DOMSignContext signContext = new DOMSignContext(privateKey, parentNode);

        XMLSignature signature = signatureFactory.newXMLSignature(signedInfo, keyInfo);

        signature.sign(signContext);

        return converterDocumentParaString(document);
    }

    private Document carregarXml(String xml) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);

        try {
            factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            factory.setFeature("http://xml.org/sax/features/external-general-entities", false);
            factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
        } catch (Exception ignored) {
            // Algumas JVMs/parsers podem nÃ£o suportar todas as features.
        }

        return factory
                .newDocumentBuilder()
                .parse(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));
    }

    private String converterDocumentParaString(Document document) throws Exception {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();

        try {
            transformerFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
        } catch (Exception ignored) {
            // Algumas JVMs/parsers podem nÃ£o suportar essa feature.
        }

        Transformer transformer = transformerFactory.newTransformer();

        transformer.setOutputProperty(OutputKeys.INDENT, "no");
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

        StringWriter writer = new StringWriter();

        transformer.transform(
                new DOMSource(document),
                new StreamResult(writer)
        );

        return writer
                .toString()
                .replaceAll(">\\s+<", "><")
                .replace("&#13;", "")
                .replace("\r", "")
                .replace("\n", "")
                .replace("\t", "")
                .trim();
    }

    private void validarParametros(
            String xml,
            String tag,
            String caminhoCertificado,
            String senha
    ) {
        if (xml == null || xml.trim().isEmpty()) {
            throw new IllegalArgumentException("XML nÃ£o informado para assinatura.");
        }

        if (tag == null || tag.trim().isEmpty()) {
            throw new IllegalArgumentException("Tag de assinatura nÃ£o informada.");
        }

        if (caminhoCertificado == null || caminhoCertificado.trim().isEmpty()) {
            throw new IllegalArgumentException("Caminho do certificado digital nÃ£o informado.");
        }

        if (senha == null || senha.trim().isEmpty()) {
            throw new IllegalArgumentException("Senha do certificado digital nÃ£o informada.");
        }
    }

    private KeyStore carregarCertificado(String caminhoCertificado, char[] senha) throws Exception {
        File arquivoCertificado = new File(caminhoCertificado);

        if (!arquivoCertificado.exists()) {
            throw new FileNotFoundException(
                    "Certificado digital nÃ£o encontrado em: " + caminhoCertificado
            );
        }

        if (!arquivoCertificado.isFile()) {
            throw new IllegalArgumentException(
                    "O caminho do certificado nÃ£o Ã© um arquivo vÃ¡lido: " + caminhoCertificado
            );
        }

        KeyStore keyStore = KeyStore.getInstance("PKCS12");

        try (FileInputStream fis = new FileInputStream(arquivoCertificado)) {
            keyStore.load(fis, senha);
        }

        return keyStore;
    }

    private String buscarAliasComChavePrivada(KeyStore keyStore) throws Exception {
        Enumeration<String> aliases = keyStore.aliases();

        while (aliases.hasMoreElements()) {
            String alias = aliases.nextElement();

            if (keyStore.isKeyEntry(alias)) {
                return alias;
            }
        }

        return null;
    }
    
    public String assinarDocumentoSemId(
            String xml,
            String caminhoCertificado,
            String senhaCertificado
    ) {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);

            Document document = dbf.newDocumentBuilder()
                    .parse(new InputSource(new StringReader(xml)));

            XMLSignatureFactory signatureFactory = XMLSignatureFactory.getInstance("DOM");

            Transform envelopedTransform = signatureFactory.newTransform(
                    Transform.ENVELOPED,
                    (TransformParameterSpec) null
            );

            Reference reference = signatureFactory.newReference(
                    "",
                    signatureFactory.newDigestMethod(DigestMethod.SHA1, null),
                    Collections.singletonList(envelopedTransform),
                    null,
                    null
            );

            SignedInfo signedInfo = signatureFactory.newSignedInfo(
                    signatureFactory.newCanonicalizationMethod(
                            CanonicalizationMethod.INCLUSIVE,
                            (C14NMethodParameterSpec) null
                    ),
                    signatureFactory.newSignatureMethod(SignatureMethod.RSA_SHA1, null),
                    Collections.singletonList(reference)
            );

            KeyStore keyStore = KeyStore.getInstance("PKCS12");

            try (FileInputStream fis = new FileInputStream(caminhoCertificado)) {
                keyStore.load(fis, senhaCertificado.toCharArray());
            }

            String alias = keyStore.aliases().nextElement();

            PrivateKey privateKey = (PrivateKey) keyStore.getKey(
                    alias,
                    senhaCertificado.toCharArray()
            );

            X509Certificate certificate = (X509Certificate) keyStore.getCertificate(alias);

            KeyInfoFactory keyInfoFactory = signatureFactory.getKeyInfoFactory();

            X509Data x509Data = keyInfoFactory.newX509Data(
                    Collections.singletonList(certificate)
            );

            KeyInfo keyInfo = keyInfoFactory.newKeyInfo(
                    Collections.singletonList(x509Data)
            );

            DOMSignContext signContext = new DOMSignContext(
                    privateKey,
                    document.getDocumentElement()
            );

            XMLSignature signature = signatureFactory.newXMLSignature(
                    signedInfo,
                    keyInfo
            );

            signature.sign(signContext);

            return transformarDocumentEmString(document);

        } catch (Exception e) {
            throw new RuntimeException("Erro ao assinar XML de consulta de URL da NFS-e", e);
        }
    }
    private String transformarDocumentEmString(Document document) throws Exception {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();

        Transformer transformer = transformerFactory.newTransformer();

        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

        StringWriter writer = new StringWriter();

        transformer.transform(
                new DOMSource(document),
                new StreamResult(writer)
        );

        return writer.toString();
    }
    
}
