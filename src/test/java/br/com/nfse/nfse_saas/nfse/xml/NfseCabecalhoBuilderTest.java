package br.com.nfse.nfse_saas.nfse.xml;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class NfseCabecalhoBuilderTest {

    @Test
    void geraCabecalhoNoLayoutAbrasf204() {
        String cabecalho = new NfseCabecalhoBuilder().gerarCabecalho();

        assertThat(cabecalho)
                .contains("versao=\"2.04\"")
                .contains("<versaoDados>2.04</versaoDados>");
    }
}
