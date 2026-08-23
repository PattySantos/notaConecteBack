package br.com.nfse.nfse_saas.nfse.xml;

import static org.assertj.core.api.Assertions.assertThat;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import br.com.nfse.nfse_saas.nfse.dto.NotaFiscalServicoDTO;

class NfseXmlBuilderTest {
    @Test
    void usaParametrosTributariosInformadosSemFixarSimplesOuIncentivo() {
        NotaFiscalServicoDTO dto = dtoMinimo();
        dto.getNotaFiscal().setRegimeEspecialTributacao(3);
        dto.getNotaFiscal().setOptanteSimplesNacional("2");
        dto.getNotaFiscal().setIncentivoFiscal("2");
        String xml = new NfseXmlBuilder().gerarXml(dto);
        assertThat(xml).contains("<RegimeEspecialTributacao>3</RegimeEspecialTributacao>");
        assertThat(xml).contains("<OptanteSimplesNacional>2</OptanteSimplesNacional>");
        assertThat(xml).contains("<IncentivoFiscal>2</IncentivoFiscal>");
    }

    @Test
    void omiteRegimeEspecialQuandoEmpresaNaoPossui() {
        assertThat(new NfseXmlBuilder().gerarXml(dtoMinimo())).doesNotContain("<RegimeEspecialTributacao>");
    }

    private NotaFiscalServicoDTO dtoMinimo() {
        NotaFiscalServicoDTO dto = new NotaFiscalServicoDTO();
        dto.getNotaFiscal().setNumeroRps("1");
        dto.getNotaFiscal().setSerieRps("1");
        dto.getNotaFiscal().setDataCompetencia(LocalDate.of(2026, 8, 23));
        dto.getNotaFiscal().setValorServicos(new BigDecimal("100.00"));
        dto.getNotaFiscal().setAliquotaIss(new BigDecimal("5.00"));
        dto.getNotaFiscal().setCodigoItemListaServico("01.01");
        dto.getNotaFiscal().setCodigoTributacaoMunicipio("0101");
        dto.getNotaFiscal().setCodigoCnae("6201501");
        dto.getNotaFiscal().setDiscriminacaoServico("Desenvolvimento de software");
        dto.getPrestador().setCpfCnpjPrestador("12345678000190");
        dto.getPrestador().setInscricaoMunicipalPrestador("12345");
        dto.getTomador().setCpfCnpjTomador("98765432000110");
        dto.getTomador().setRazaoSocialTomador("Tomador Teste");
        dto.getTomador().setEnderecoTomador("Rua Teste");
        dto.getTomador().setNumero("10");
        dto.getTomador().setBairroTomador("Centro");
        dto.getTomador().setCodigoMunicipioTomador("5208707");
        dto.getTomador().setUfTomador("GO");
        dto.getTomador().setCepTomador("74000000");
        return dto;
    }
}
