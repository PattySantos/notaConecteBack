package br.com.nfse.nfse_saas.controller;

import br.com.nfse.nfse_saas.domain.Empresa;
import br.com.nfse.nfse_saas.domain.NotaFiscal;
import br.com.nfse.nfse_saas.nfse.dto.NfseEmissaoResponseDTO;
import br.com.nfse.nfse_saas.nfse.service.NfseEmissaoService;
import br.com.nfse.nfse_saas.repository.EmpresaRepository;
import br.com.nfse.nfse_saas.repository.NotaFiscalRepository;
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
        preencherNumeroNfse(nota);
        return repository.save(nota);
    }

    @PostMapping("/{id}/emitir-nfse")
    public NfseEmissaoResponseDTO emitirNfse(@PathVariable Long id) {
        return nfseEmissaoService.emitir(id);
    }

    @GetMapping("/empresa/{empresaId}")
    public List<NotaFiscal> listarPorEmpresa(@PathVariable Long empresaId) {
        return repository.findByEmpresaId(empresaId);
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