package br.com.nfse.nfse_saas.nfse.service;

import org.springframework.stereotype.Service;

import br.com.nfse.nfse_saas.nfse.util.NfseXmlUtil;

@Service
public class NfsePdfService {

    public NfseResumoPdfDTO extrairResumo(String xmlAutorizado) {
        if (xmlAutorizado == null || xmlAutorizado.trim().isEmpty()) {
            throw new IllegalArgumentException("XML autorizado da NFS-e nao informado.");
        }
        NfseResumoPdfDTO resumo = new NfseResumoPdfDTO();
        resumo.setNumeroNfse(NfseXmlUtil.extrairTagDentroDe(xmlAutorizado, "InfNfse", "Numero"));
        resumo.setCodigoVerificacao(NfseXmlUtil.extrairTagDentroDe(xmlAutorizado, "InfNfse", "CodigoVerificacao"));
        resumo.setDataEmissao(NfseXmlUtil.extrairTagDentroDe(xmlAutorizado, "InfNfse", "DataEmissao"));
        resumo.setCompetencia(NfseXmlUtil.extrairTagDentroDe(xmlAutorizado, "InfDeclaracaoPrestacaoServico", "Competencia"));
        resumo.setDiscriminacao(NfseXmlUtil.extrairTagDentroDe(xmlAutorizado, "Servico", "Discriminacao"));
        resumo.setValorServicos(NfseXmlUtil.extrairTagDentroDe(xmlAutorizado, "Valores", "ValorServicos"));
        resumo.setValorIss(NfseXmlUtil.extrairTagDentroDe(xmlAutorizado, "ValoresNfse", "ValorIss"));
        return resumo;
    }
}
