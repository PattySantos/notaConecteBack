package br.com.nfse.nfse_saas.nfse.service;

import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;

import org.springframework.stereotype.Service;
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

    public NfseEmissaoService(
            NotaFiscalRepository notaRepository,
            ClienteRepository clienteRepository,
            NfseService nfseService
    ) {
        this.notaRepository = notaRepository;
        this.clienteRepository = clienteRepository;
        this.nfseService = nfseService;
    }

    public NfseEmissaoResponseDTO emitir(Long notaId) {
        NotaFiscal nota = notaRepository.findById(notaId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "NFS-e nao localizada."));

        Empresa empresa = nota.getEmpresa();

        if (empresa == null || empresa.getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A NFS-e nao possui empresa vinculada.");
        }

        if (nota.getClienteId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A NFS-e nao possui tomador vinculado.");
        }

        Cliente cliente = clienteRepository.findById(nota.getClienteId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tomador da NFS-e nao localizado."));

        validarCertificado(empresa);

        Path certificadoTemporario = null;

        try {
            certificadoTemporario = criarCertificadoTemporario(empresa);
            NotaFiscalServicoDTO dto = montarDto(nota, empresa, cliente);
            String retorno = nfseService.emitir(dto, certificadoTemporario.toString(), empresa.getSenhaCertificadoDigital());

            nota.setStatus("EMITIDA");
            nota.setRetornoEmissaoNfse(retorno);
            notaRepository.save(nota);

            return new NfseEmissaoResponseDTO(nota.getId(), nota.getNumeroNfse(), nota.getStatus(), retorno);
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

        dto.getNotaFiscal().setNumeroRps(String.valueOf(nota.getNumeroNfse() != null ? nota.getNumeroNfse() : nota.getId()));
        dto.getNotaFiscal().setSerieRps("1");
        dto.getNotaFiscal().setTipoRps("1");
        dto.getNotaFiscal().setValorServicos(nota.getValor() != null ? nota.getValor() : BigDecimal.ZERO);
        dto.getNotaFiscal().setAliquotaIss(nota.getIss() != null ? nota.getIss() : BigDecimal.ZERO);
        dto.getNotaFiscal().setIssRetido("2");
        dto.getNotaFiscal().setCodigoItemListaServico(primeiroTexto(nota.getCodigoServico(), nota.getCodigoServicoNacional()));
        dto.getNotaFiscal().setCodigoNbs(nota.getCodigoNbs());
        dto.getNotaFiscal().setDiscriminacaoServico(nota.getDescricao());

        dto.getPrestador().setCpfCnpjPrestador(empresa.getCnpj());
        dto.getPrestador().setInscricaoMunicipalPrestador("");
        dto.getPrestador().setCodigoCnae("");
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
}
