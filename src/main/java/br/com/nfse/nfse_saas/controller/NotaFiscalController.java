package br.com.nfse.nfse_saas.controller;

import br.com.nfse.nfse_saas.domain.Empresa;
import br.com.nfse.nfse_saas.domain.NotaFiscal;
import br.com.nfse.nfse_saas.nfse.dto.NfseEmissaoResponseDTO;
import br.com.nfse.nfse_saas.nfse.service.NfseEmissaoService;
import br.com.nfse.nfse_saas.repository.EmpresaRepository;
import br.com.nfse.nfse_saas.repository.NotaFiscalRepository;
import br.com.nfse.nfse_saas.tenant.TenantContext;
import org.springframework.web.bind.annotation.*;

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
    public NotaFiscal salvar(@RequestBody NotaFiscal nota) {
        nota.setEmpresa(empresaRepository.getReferenceById(TenantContext.getTenantId()));
        preencherNumeroNfse(nota);
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

    private void preencherNumeroNfse(NotaFiscal nota) {
        if (nota.getId() != null || nota.getNumeroNfse() != null || nota.getEmpresa() == null || nota.getEmpresa().getId() == null) {
            return;
        }

        Empresa empresa = empresaRepository.findById(nota.getEmpresa().getId()).orElse(null);

        if (empresa == null) {
            return;
        }

        Long proximoNumero = empresa.getProximoNumeroNfse();

        if (proximoNumero == null || proximoNumero < 1) {
            proximoNumero = 1L;
        }

        nota.setNumeroNfse(proximoNumero);
        empresa.setProximoNumeroNfse(proximoNumero + 1);
        empresaRepository.save(empresa);
    }
}
