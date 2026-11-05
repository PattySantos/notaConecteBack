package br.com.nfse.nfse_saas.nfse.xml;



public class NfseCabecalhoBuilder {

    public String gerarCabecalho() {
        return "<cabecalho versao=\"2.04\" xmlns=\"http://www.abrasf.org.br/nfse.xsd\">"
                + "<versaoDados>2.04</versaoDados>"
                + "</cabecalho>";
    }
}

