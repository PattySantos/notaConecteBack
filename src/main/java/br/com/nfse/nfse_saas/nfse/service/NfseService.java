package br.com.nfse.nfse_saas.nfse.service;

import org.springframework.stereotype.Service;

import br.com.nfse.nfse_saas.nfse.assinatura.XmlSigner;
import br.com.nfse.nfse_saas.nfse.dto.NotaFiscalServicoDTO;
import br.com.nfse.nfse_saas.nfse.soap.NfseSoapClient;
import br.com.nfse.nfse_saas.nfse.xml.NfseCabecalhoBuilder;
import br.com.nfse.nfse_saas.nfse.xml.NfseXmlBuilder;

@Service
public class NfseService {

    private static final String TAG_ASSINATURA_RPS = "InfDeclaracaoPrestacaoServico";

    private final NfseXmlBuilder xmlBuilder;
    private final NfseCabecalhoBuilder cabecalhoBuilder;
    private final NfseSoapClient soapClient;
    private final XmlSigner xmlSigner;

    public NfseService(NfseSoapClient soapClient) {
        this.xmlBuilder = new NfseXmlBuilder();
        this.cabecalhoBuilder = new NfseCabecalhoBuilder();
        this.soapClient = soapClient;
        this.xmlSigner = new XmlSigner();
    }

    public String gerarXml(NotaFiscalServicoDTO dto) {
        return xmlBuilder.gerarXml(dto);
    }

    public String assinarXml(String xml, String certificadoPath, String senhaCertificado) throws Exception {
        return assinarXml(xml, TAG_ASSINATURA_RPS, certificadoPath, senhaCertificado);
    }

    public String assinarXml(String xml, String tagAssinatura, String certificadoPath, String senhaCertificado) throws Exception {
        return xmlSigner.assinar(xml, tagAssinatura, certificadoPath, senhaCertificado);
    }

    public String emitir(NotaFiscalServicoDTO dto, String ambiente, String certificadoPath, String senhaCertificado) throws Exception {
        String xml = gerarXml(dto);
        String xmlAssinado = assinarXml(xml, certificadoPath, senhaCertificado);
        return soapClient.enviar(ambiente, cabecalhoBuilder.gerarCabecalho(), xmlAssinado);
    }
}
