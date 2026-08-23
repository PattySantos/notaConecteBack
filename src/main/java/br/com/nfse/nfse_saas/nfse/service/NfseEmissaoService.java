package br.com.nfse.nfse_saas.nfse.service;

import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import br.com.nfse.nfse_saas.domain.Cliente;
import br.com.nfse.nfse_saas.domain.Empresa;
import br.com.nfse.nfse_saas.domain.Endereco;
import br.com.nfse.nfse_saas.domain.NotaFiscal;
import br.com.nfse.nfse_saas.nfse.dto.NfseEmissaoResponseDTO;
import br.com.nfse.nfse_saas.nfse.dto.NotaFiscalServicoDTO;
import br.com.nfse.nfse_saas.repository.ClienteRepository;
import br.com.nfse.nfse_saas.repository.NotaFiscalRepository;

@Service
public class NfseEmissaoService {

    private final NotaFiscalRepository notaRepository;
    private final ClienteRepository clienteRepository;
    private final NfseService nfseService;
    private final NfseResponseParser responseParser;

    public NfseEmissaoService(
            NotaFiscalRepository notaRepository,
            ClienteRepository clienteRepository,
            NfseService nfseService,
            NfseResponseParser responseParser
    ) {
        this.notaRepository = notaRepository;
        this.clienteRepository = clienteRepository;
        this.nfseService = nfseService;
        this.responseParser = responseParser;
    }

    @Transactional(noRollbackFor = ResponseStatusException.class)
    public NfseEmissaoResponseDTO emitir(Long notaId) {
        NotaFiscal nota = notaRepository.findByIdForUpdate(notaId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "NFS-e nao localizada."));

        Empresa empresa = nota.getEmpresa();

        if ("EMITIDA".equals(nota.getStatus())) {
            return new NfseEmissaoResponseDTO(nota.getId(), nota.getNumeroNfse(), nota.getStatus(), "NFS-e ja emitida.");
        }
        if ("EMITINDO".equals(nota.getStatus())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "A NFS-e ja esta em processamento.");
        }

        if (empresa == null || empresa.getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A NFS-e nao possui empresa vinculada.");
        }

        if (nota.getClienteId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A NFS-e nao possui tomador vinculado.");
        }

        Cliente cliente = clienteRepository.findById(nota.getClienteId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tomador da NFS-e nao localizado."));

        validarCertificado(empresa);
        validarDadosFiscais(nota, empresa, cliente);
        nota.setStatus("EMITINDO");
        notaRepository.saveAndFlush(nota);

        Path certificadoTemporario = null;

        try {
            certificadoTemporario = criarCertificadoTemporario(empresa);
            NotaFiscalServicoDTO dto = montarDto(nota, empresa, cliente);
            String retorno = nfseService.emitir(dto, empresa.getAmbienteEmissao(), certificadoTemporario.toString(), empresa.getSenhaCertificadoDigital());
            NfseMunicipalResponse resposta = responseParser.parse(retorno);

            if (!resposta.autorizada()) {
                nota.setStatus("REJEITADA");
                nota.setRetornoEmissaoNfse(resposta.mensagem());
                notaRepository.save(nota);
                throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, resposta.mensagem());
            }

            nota.setStatus("EMITIDA");
            nota.setNumeroNfse(resposta.numeroNfse());
            nota.setCodigoVerificacao(resposta.codigoVerificacao());
            nota.setRetornoEmissaoNfse(resposta.xmlNfse());
            notaRepository.save(nota);

            return new NfseEmissaoResponseDTO(nota.getId(), nota.getNumeroNfse(), nota.getStatus(), resposta.mensagem());
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            nota.setStatus("ERRO_EMISSAO");
            nota.setRetornoEmissaoNfse(e.getMessage());
            notaRepository.save(nota);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Erro ao emitir NFS-e: " + e.getMessage(), e);
        } finally {
            if (certificadoTemporario != null) {
                try {
                    Files.deleteIfExists(certificadoTemporario);
                } catch (Exception ignored) {
                }
            }
        }
    }

    private NotaFiscalServicoDTO montarDto(NotaFiscal nota, Empresa empresa, Cliente cliente) {
        NotaFiscalServicoDTO dto = new NotaFiscalServicoDTO();
        Endereco enderecoTomador = cliente.getEndereco();

        dto.getNotaFiscal().setNumeroRps(primeiroTexto(nota.getNumeroRps(), String.valueOf(nota.getNumeroNfse() != null ? nota.getNumeroNfse() : nota.getId())));
        dto.getNotaFiscal().setSerieRps(primeiroTexto(nota.getSerieRps(), "1"));
        dto.getNotaFiscal().setTipoRps(String.valueOf(nota.getTipoRps() != null ? nota.getTipoRps() : 1));
        dto.getNotaFiscal().setDataCompetencia(nota.getDataCompetencia());
        dto.getNotaFiscal().setValorServicos(nota.getValor() != null ? nota.getValor() : BigDecimal.ZERO);
        dto.getNotaFiscal().setAliquotaIss(nota.getIss() != null ? nota.getIss() : BigDecimal.ZERO);
        dto.getNotaFiscal().setIssRetido(Boolean.TRUE.equals(nota.getIssRetido()) ? "1" : "2");
        dto.getNotaFiscal().setCodigoItemListaServico(primeiroTexto(nota.getCodigoServico(), nota.getCodigoServicoNacional()));
        dto.getNotaFiscal().setCodigoTributacaoMunicipio(nota.getCodigoServico());
        dto.getNotaFiscal().setCodigoCnae(nota.getCodigoCnae());
        dto.getNotaFiscal().setCodigoNbs(nota.getCodigoNbs());
        dto.getNotaFiscal().setDiscriminacaoServico(nota.getDescricao());
        dto.getNotaFiscal().setCodigoMunicipio(primeiroTexto(nota.getMunicipio(), "5208707"));
        dto.getNotaFiscal().setExigibilidadeIss(nota.getExigibilidadeIss() != null ? nota.getExigibilidadeIss() : 1);
        dto.getNotaFiscal().setValorDeducoes(nota.getValorDeducoes());
        dto.getNotaFiscal().setDescontoIncondicionado(nota.getDescontoIncondicionado());
        dto.getNotaFiscal().setDescontoCondicionado(nota.getDescontoCondicionado());
        dto.getNotaFiscal().setValorPis(nota.getValorPis());
        dto.getNotaFiscal().setValorCofins(nota.getValorCofins());
        dto.getNotaFiscal().setValorInss(nota.getValorInss());
        dto.getNotaFiscal().setValorIr(nota.getValorIr());
        dto.getNotaFiscal().setValorCsll(nota.getValorCsll());
        dto.getNotaFiscal().setOutrasRetencoes(nota.getOutrasRetencoes());
        dto.getNotaFiscal().setProcessoSuspensao(nota.getProcessoSuspensao());
        dto.getNotaFiscal().setCno(nota.getCno());
        dto.getNotaFiscal().setArt(nota.getArt());
        dto.getNotaFiscal().setInformacoesComplementares(nota.getInformacoesComplementares());
        dto.getNotaFiscal().setRegimeEspecialTributacao(regimeEspecial(empresa.getRegimeEspecialTributacao()));
        dto.getNotaFiscal().setOptanteSimplesNacional(Boolean.TRUE.equals(empresa.getOptanteSimplesNacional()) ? "1" : "2");
        dto.getNotaFiscal().setIncentivoFiscal(Boolean.TRUE.equals(empresa.getIncentivoFiscal()) ? "1" : "2");

        dto.getPrestador().setCpfCnpjPrestador(empresa.getCnpj());
        dto.getPrestador().setInscricaoMunicipalPrestador(empresa.getInscricaoMunicipal());
        dto.getPrestador().setCodigoCnae(primeiroTexto(nota.getCodigoCnae(), empresa.getCnaePrincipal()));
        dto.getPrestador().setCodigoTributacaoMunicipio(primeiroTexto(nota.getCodigoServico(), nota.getCodigoServicoNacional()));

        dto.getTomador().setCpfCnpjTomador(cliente.getDocumento());
        dto.getTomador().setInscricaoMunicipalTomador(cliente.getInscricaoMunicipal());
        dto.getTomador().setRazaoSocialTomador(cliente.getNome());
        dto.getTomador().setTelefoneTomador(cliente.getTelefone());
        dto.getTomador().setEmailTomador(cliente.getEmail());

        if (enderecoTomador != null) {
            dto.getTomador().setEnderecoTomador(enderecoTomador.getLogradouro());
            dto.getTomador().setNumero(enderecoTomador.getNumero());
            dto.getTomador().setBairroTomador(enderecoTomador.getBairro());
            dto.getTomador().setCodigoMunicipioTomador(primeiroTexto(enderecoTomador.getCodigoMunicipioIbge(), "5208707"));
            dto.getTomador().setUfTomador(primeiroTexto(enderecoTomador.getEstado(), "GO"));
            dto.getTomador().setCepTomador(enderecoTomador.getCep());
        }

        return dto;
    }

    private void validarCertificado(Empresa empresa) {
        if (vazio(empresa.getCertificadoDigitalBase64())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cadastre o certificado digital A1 da empresa antes de emitir a NFS-e.");
        }

        if (vazio(empresa.getSenhaCertificadoDigital())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Informe a senha do certificado digital da empresa antes de emitir a NFS-e.");
        }
    }

    private void validarDadosFiscais(NotaFiscal nota, Empresa empresa, Cliente cliente) {
        if (vazio(empresa.getCnpj()) || vazio(empresa.getInscricaoMunicipal())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "CNPJ e inscricao municipal do prestador sao obrigatorios.");
        }
        if (vazio(nota.getNumeroRps()) || vazio(nota.getSerieRps())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Numero e serie do RPS sao obrigatorios.");
        }
        if (nota.getValor() == null || nota.getValor().signum() <= 0 || vazio(nota.getDescricao())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Servico e valor da NFS-e sao obrigatorios.");
        }
        if (vazio(nota.getCodigoServicoNacional()) || vazio(nota.getCodigoServico()) || vazio(nota.getCodigoCnae())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Item LC 116, codigo municipal e CNAE sao obrigatorios.");
        }
        if (vazio(cliente.getDocumento()) || vazio(cliente.getNome())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Documento e nome do tomador sao obrigatorios.");
        }
    }

    private Path criarCertificadoTemporario(Empresa empresa) throws Exception {
        String nome = empresa.getCertificadoDigitalNome();
        String sufixo = nome != null && nome.toLowerCase().endsWith(".p12") ? ".p12" : ".pfx";
        Path arquivo = Files.createTempFile("nfse-certificado-", sufixo);
        Files.write(arquivo, Base64.getDecoder().decode(empresa.getCertificadoDigitalBase64()));
        return arquivo;
    }

    private boolean vazio(String valor) {
        return valor == null || valor.trim().isEmpty();
    }

    private String primeiroTexto(String valor, String fallback) {
        return !vazio(valor) ? valor : fallback;
    }

    private Integer regimeEspecial(String regime) {
        if (vazio(regime)) return null;
        return switch (regime) {
            case "MICROEMPRESA_MUNICIPAL" -> 1;
            case "ESTIMATIVA" -> 2;
            case "SOCIEDADE_PROFISSIONAIS" -> 3;
            case "COOPERATIVA" -> 4;
            case "MEI" -> 5;
            case "ME_EPP_SIMPLES" -> 6;
            default -> throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Regime especial de tributacao invalido.");
        };
    }
}
