package br.com.nfse.nfse_saas.nfse.xml;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.Normalizer;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import br.com.nfse.nfse_saas.nfse.dto.NotaFiscalServicoDTO;

public class NfseXmlBuilder {

    public String gerarXml(NotaFiscalServicoDTO dto) {
        String data = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String competencia = dto.getNotaFiscal().getDataCompetencia() != null ? dto.getNotaFiscal().getDataCompetencia().toString() : data;
        DecimalFormatSymbols symbols = DecimalFormatSymbols.getInstance(Locale.US);
        DecimalFormat df2 = new DecimalFormat("0.00", symbols);
        DecimalFormat dfAliquota = new DecimalFormat("0.##", symbols);

        BigDecimal valorServicos = valor(dto.getNotaFiscal().getValorServicos());
        BigDecimal aliquota = valor(dto.getNotaFiscal().getAliquotaIss());
        BigDecimal baseCalculo = valorServicos.subtract(valor(dto.getNotaFiscal().getValorDeducoes())).subtract(valor(dto.getNotaFiscal().getDescontoIncondicionado())).max(BigDecimal.ZERO);
        BigDecimal valorIss = baseCalculo.multiply(aliquota).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);

        String cnpjPrestador = somenteNumeros(dto.getPrestador().getCpfCnpjPrestador());
        String inscricaoMunicipal = somenteNumeros(dto.getPrestador().getInscricaoMunicipalPrestador());
        String documentoTomador = somenteNumeros(dto.getTomador().getCpfCnpjTomador());
        String cep = somenteNumeros(dto.getTomador().getCepTomador());
        String numeroRps = somenteNumeros(dto.getNotaFiscal().getNumeroRps());
        String serieRps = texto(dto.getNotaFiscal().getSerieRps(), "1");
        String tipoRps = texto(dto.getNotaFiscal().getTipoRps(), "1");
        String idInfDeclaracao = numeroRps + "_" + somenteNumeros(serieRps) + "_" + tipoRps;
        String issRetido = texto(dto.getNotaFiscal().getIssRetido(), "2");
        String codigoMunicipioTomador = texto(dto.getTomador().getCodigoMunicipioTomador(), "5208707");

        StringBuilder xml = new StringBuilder();
        xml.append("<GerarNfseEnvio xmlns=\"http://www.abrasf.org.br/nfse.xsd\">");
        xml.append("<Rps>");
        xml.append("<InfDeclaracaoPrestacaoServico Id=\"").append(idInfDeclaracao).append("\">");
        xml.append("<Rps><IdentificacaoRps>");
        xml.append("<Numero>").append(numeroRps).append("</Numero>");
        xml.append("<Serie>").append(escaparXml(serieRps)).append("</Serie>");
        xml.append("<Tipo>").append(tipoRps).append("</Tipo>");
        xml.append("</IdentificacaoRps><DataEmissao>").append(data).append("</DataEmissao><Status>1</Status></Rps>");
        xml.append("<Competencia>").append(competencia).append("</Competencia>");
        xml.append("<Servico><Valores>");
        xml.append("<ValorServicos>").append(df2.format(valorServicos)).append("</ValorServicos>");
        xml.append("<ValorDeducoes>").append(df2.format(valor(dto.getNotaFiscal().getValorDeducoes()))).append("</ValorDeducoes>");
        xml.append("<ValorPis>").append(df2.format(valor(dto.getNotaFiscal().getValorPis()))).append("</ValorPis>");
        xml.append("<ValorCofins>").append(df2.format(valor(dto.getNotaFiscal().getValorCofins()))).append("</ValorCofins>");
        xml.append("<ValorInss>").append(df2.format(valor(dto.getNotaFiscal().getValorInss()))).append("</ValorInss>");
        xml.append("<ValorIr>").append(df2.format(valor(dto.getNotaFiscal().getValorIr()))).append("</ValorIr>");
        xml.append("<ValorCsll>").append(df2.format(valor(dto.getNotaFiscal().getValorCsll()))).append("</ValorCsll>");
        xml.append("<OutrasRetencoes>").append(df2.format(valor(dto.getNotaFiscal().getOutrasRetencoes()))).append("</OutrasRetencoes><ValTotTributos>0.00</ValTotTributos>");
        xml.append("<ValorIss>").append(df2.format(valorIss)).append("</ValorIss>");
        xml.append("<Aliquota>").append(dfAliquota.format(aliquota)).append("</Aliquota>");
        xml.append("<DescontoIncondicionado>").append(df2.format(valor(dto.getNotaFiscal().getDescontoIncondicionado()))).append("</DescontoIncondicionado>");
        xml.append("<DescontoCondicionado>").append(df2.format(valor(dto.getNotaFiscal().getDescontoCondicionado()))).append("</DescontoCondicionado>");
        xml.append("</Valores>");
        xml.append("<IssRetido>").append(issRetido).append("</IssRetido>");
        if ("1".equals(issRetido)) {
            xml.append("<ResponsavelRetencao>1</ResponsavelRetencao>");
        }
        xml.append("<ItemListaServico>").append(escaparXml(dto.getNotaFiscal().getCodigoItemListaServico())).append("</ItemListaServico>");
        xml.append("<CodigoCnae>").append(somenteNumeros(dto.getNotaFiscal().getCodigoCnae())).append("</CodigoCnae>");
        xml.append("<CodigoTributacaoMunicipio>").append(somenteNumeros(dto.getNotaFiscal().getCodigoTributacaoMunicipio())).append("</CodigoTributacaoMunicipio>");
        if (!texto(dto.getNotaFiscal().getCodigoNbs(), "").isEmpty()) {
            xml.append("<CodigoNbs>").append(escaparXml(dto.getNotaFiscal().getCodigoNbs())).append("</CodigoNbs>");
        }
        xml.append("<Discriminacao>").append(escaparXml(removerAcentos(dto.getNotaFiscal().getDiscriminacaoServico()))).append("</Discriminacao>");
        String codigoMunicipio = somenteNumeros(texto(dto.getNotaFiscal().getCodigoMunicipio(), "5208707"));
        int exigibilidadeIss = dto.getNotaFiscal().getExigibilidadeIss() != null ? dto.getNotaFiscal().getExigibilidadeIss() : 1;
        xml.append("<CodigoMunicipio>").append(codigoMunicipio).append("</CodigoMunicipio><ExigibilidadeISS>").append(exigibilidadeIss).append("</ExigibilidadeISS>");
        if ((exigibilidadeIss == 6 || exigibilidadeIss == 7) && !texto(dto.getNotaFiscal().getProcessoSuspensao(), "").isEmpty()) {
            xml.append("<ProcessoSuspensao>").append(escaparXml(dto.getNotaFiscal().getProcessoSuspensao())).append("</ProcessoSuspensao>");
        }
        xml.append("<MunicipioIncidencia>").append(codigoMunicipio).append("</MunicipioIncidencia>");
        xml.append("</Servico>");
        xml.append("<Prestador><CpfCnpj><Cnpj>").append(cnpjPrestador).append("</Cnpj></CpfCnpj>");
        xml.append("<InscricaoMunicipal>").append(inscricaoMunicipal).append("</InscricaoMunicipal></Prestador>");
        xml.append("<TomadorServico><IdentificacaoTomador><CpfCnpj>");
        if (documentoTomador.length() == 11) {
            xml.append("<Cpf>").append(documentoTomador).append("</Cpf>");
        } else {
            xml.append("<Cnpj>").append(documentoTomador).append("</Cnpj>");
        }
        xml.append("</CpfCnpj>");
        String inscricaoMunicipalTomador = somenteNumeros(dto.getTomador().getInscricaoMunicipalTomador());
        if (!inscricaoMunicipalTomador.isEmpty()) {
            xml.append("<InscricaoMunicipal>").append(inscricaoMunicipalTomador).append("</InscricaoMunicipal>");
        }
        xml.append("</IdentificacaoTomador>");
        xml.append("<RazaoSocial>").append(escaparXml(dto.getTomador().getRazaoSocialTomador())).append("</RazaoSocial>");
        xml.append("<Endereco><Endereco>").append(escaparXml(dto.getTomador().getEnderecoTomador())).append("</Endereco>");
        xml.append("<Numero>").append(escaparXml(dto.getTomador().getNumero())).append("</Numero>");
        xml.append("<Bairro>").append(escaparXml(removerAcentos(dto.getTomador().getBairroTomador()))).append("</Bairro>");
        xml.append("<CodigoMunicipio>").append(somenteNumeros(codigoMunicipioTomador)).append("</CodigoMunicipio>");
        xml.append("<Uf>").append(escaparXml(dto.getTomador().getUfTomador())).append("</Uf>");
        xml.append("<Cep>").append(cep).append("</Cep></Endereco>");
        String telefoneTomador = somenteNumeros(dto.getTomador().getTelefoneTomador());
        String emailTomador = texto(dto.getTomador().getEmailTomador(), "");
        if (!telefoneTomador.isEmpty() || !emailTomador.isEmpty()) {
            xml.append("<Contato>");
            if (!telefoneTomador.isEmpty()) xml.append("<Telefone>").append(telefoneTomador).append("</Telefone>");
            if (!emailTomador.isEmpty()) xml.append("<Email>").append(escaparXml(emailTomador)).append("</Email>");
            xml.append("</Contato>");
        }
        xml.append("</TomadorServico>");
        if (!texto(dto.getNotaFiscal().getCno(), "").isEmpty() || !texto(dto.getNotaFiscal().getArt(), "").isEmpty()) {
            xml.append("<ConstrucaoCivil><CodigoObra>").append(escaparXml(dto.getNotaFiscal().getCno())).append("</CodigoObra><Art>").append(escaparXml(dto.getNotaFiscal().getArt())).append("</Art></ConstrucaoCivil>");
        }
        xml.append("<RegimeEspecialTributacao>6</RegimeEspecialTributacao><OptanteSimplesNacional>1</OptanteSimplesNacional><IncentivoFiscal>1</IncentivoFiscal>");
        xml.append("</InfDeclaracaoPrestacaoServico></Rps></GerarNfseEnvio>");
        return xml.toString().replace("\r", "").replace("\n", "").replace("\t", "").replace("&#13;", "");
    }

    private BigDecimal valor(BigDecimal valor) {
        return valor == null ? BigDecimal.ZERO : valor;
    }

    private String somenteNumeros(String valor) {
        if (valor == null) return "";
        return valor.replaceAll("\\D", "");
    }

    private String escaparXml(String valor) {
        if (valor == null) return "";
        return valor.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;").replace("'", "&apos;");
    }

    private String removerAcentos(String valor) {
        if (valor == null) return "";
        return Normalizer.normalize(valor.replace("|", "").trim(), Normalizer.Form.NFD).replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
    }

    private String texto(String valor, String padrao) {
        return valor == null || valor.trim().isEmpty() ? padrao : valor.trim();
    }
}
