package br.com.nfse.nfse_saas.controller;

import br.com.nfse.nfse_saas.domain.Empresa;
import br.com.nfse.nfse_saas.domain.NotaFiscal;
import br.com.nfse.nfse_saas.nfse.dto.NfseEmissaoResponseDTO;
import br.com.nfse.nfse_saas.nfse.service.NfseEmissaoService;
import br.com.nfse.nfse_saas.repository.EmpresaRepository;
import br.com.nfse.nfse_saas.repository.NotaFiscalRepository;
import br.com.nfse.nfse_saas.tenant.TenantContext;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.List;

@RestController
@RequestMapping("/notas")
public class NotaFiscalController {

    private final NotaFiscalRepository repository;
    private final EmpresaRepository empresaRepository;
    private final NfseEmissaoService nfseEmissaoService;

    public NotaFiscalController(
            NotaFiscalRepository repository,
            EmpresaRepository empresaRepository,
            NfseEmissaoService nfseEmissaoService
    ) {
        this.repository = repository;
        this.empresaRepository = empresaRepository;
        this.nfseEmissaoService = nfseEmissaoService;
    }

    @PostMapping
    @Transactional
    public NotaFiscal salvar(@RequestBody NotaFiscal nota) {
        Empresa empresa = empresaRepository.findByIdForUpdate(TenantContext.getTenantId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Empresa nao encontrada"));
        nota.setId(null);
        nota.setEmpresa(empresa);
        preencherNumeroRps(nota, empresa);
        nota.setNumeroNfse(null);
        nota.setCodigoVerificacao(null);
        nota.setStatus("RASCUNHO");
        return repository.save(nota);
    }

    @PostMapping("/{id}/emitir-nfse")
    public NfseEmissaoResponseDTO emitirNfse(@PathVariable Long id) {
        repository.findByIdAndEmpresaId(id, TenantContext.getTenantId())
                .orElseThrow(() -> new RuntimeException("Nota fiscal nao encontrada para o tenant"));
        return nfseEmissaoService.emitir(id);
    }

    @GetMapping
    public List<NotaFiscal> listar() {
        return repository.findByEmpresaId(TenantContext.getTenantId());
    }

    @GetMapping("/{id}")
    public NotaFiscal buscar(@PathVariable Long id) {
        return buscarDoTenant(id);
    }

    @PutMapping("/{id}")
    public NotaFiscal atualizar(@PathVariable Long id, @RequestBody NotaFiscal dados) {
        NotaFiscal nota = buscarDoTenant(id);
        if ("EMITIDA".equals(nota.getStatus())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "NFS-e emitida nao pode ser editada.");
        }

        nota.setClienteId(dados.getClienteId());
        nota.setDescricao(dados.getDescricao());
        nota.setValor(dados.getValor());
        nota.setIss(dados.getIss());
        nota.setCodigoServico(dados.getCodigoServico());
        nota.setCodigoServicoNacional(dados.getCodigoServicoNacional());
        nota.setCodigoNbs(dados.getCodigoNbs());
        nota.setIndicadorIbscbs(dados.getIndicadorIbscbs());
        nota.setMunicipio(dados.getMunicipio());
        nota.setProdutoId(dados.getProdutoId());
        nota.setDataCompetencia(dados.getDataCompetencia());
        nota.setNumeroRps(dados.getNumeroRps());
        nota.setSerieRps(dados.getSerieRps());
        nota.setTipoRps(dados.getTipoRps());
        nota.setLocalPrestacao(dados.getLocalPrestacao());
        nota.setPaisPrestacao(dados.getPaisPrestacao());
        nota.setCodigoCnae(dados.getCodigoCnae());
        nota.setExigibilidadeIss(dados.getExigibilidadeIss());
        nota.setIssRetido(dados.getIssRetido());
        nota.setValorDeducoes(dados.getValorDeducoes());
        nota.setDescontoIncondicionado(dados.getDescontoIncondicionado());
        nota.setDescontoCondicionado(dados.getDescontoCondicionado());
        nota.setValorPis(dados.getValorPis());
        nota.setValorCofins(dados.getValorCofins());
        nota.setValorInss(dados.getValorInss());
        nota.setValorIr(dados.getValorIr());
        nota.setValorCsll(dados.getValorCsll());
        nota.setOutrasRetencoes(dados.getOutrasRetencoes());
        nota.setProcessoSuspensao(dados.getProcessoSuspensao());
        nota.setCno(dados.getCno());
        nota.setArt(dados.getArt());
        nota.setInformacoesComplementares(dados.getInformacoesComplementares());
        return repository.save(nota);
    }

    private NotaFiscal buscarDoTenant(Long id) {
        return repository.findByIdAndEmpresaId(id, TenantContext.getTenantId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Nota fiscal nao encontrada."));
    }

    private void preencherNumeroRps(NotaFiscal nota, Empresa empresa) {
        long proximoNumero = empresa.getProximoNumeroRps() == null || empresa.getProximoNumeroRps() < 1
                ? 1L
                : empresa.getProximoNumeroRps();
        nota.setNumeroRps(String.valueOf(proximoNumero));
        nota.setSerieRps("1");
        empresa.setProximoNumeroRps(proximoNumero + 1);
        empresaRepository.save(empresa);
    }
}
