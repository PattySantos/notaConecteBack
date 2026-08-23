package br.com.nfse.nfse_saas.controller;

import br.com.nfse.nfse_saas.domain.Empresa;
import br.com.nfse.nfse_saas.repository.EmpresaRepository;
import br.com.nfse.nfse_saas.repository.UsuarioRepository;
import br.com.nfse.nfse_saas.security.CertificateValidator;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/empresas")
public class EmpresaController {

    private final EmpresaRepository repository;
    private final UsuarioRepository usuarioRepository;
    private final CertificateValidator certificateValidator;

    public EmpresaController(EmpresaRepository repository, UsuarioRepository usuarioRepository,
                             CertificateValidator certificateValidator) {
        this.repository = repository;
        this.usuarioRepository = usuarioRepository;
        this.certificateValidator = certificateValidator;
    }


    @PostMapping
    public Empresa salvar(@RequestBody Empresa empresa, Principal principal) {
        empresa.setId(null);
        empresa.setUsuario(usuarioRepository.findByEmailIgnoreCase(principal.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario nao encontrado")));
        empresa.setProximoNumeroRps(1L);
        validarNovoCertificado(empresa);
        return repository.save(empresa);
    }
    @GetMapping
    public List<Empresa> listar(Principal principal) {
        return repository.findByUsuarioEmailIgnoreCase(principal.getName());
    }
    @GetMapping("/usuario/{usuarioId}")
    public List<Empresa> listarPorUsuario(@PathVariable Long usuarioId, Principal principal) {
        return repository.findByUsuarioEmailIgnoreCase(principal.getName());
    }

    @GetMapping("/{id}")
    public Empresa buscarPorId(@PathVariable Long id, Principal principal) {
        return buscarEmpresaDoUsuario(id, principal);
    }

    @PutMapping("/{id}")
    public Empresa atualizar(@PathVariable Long id, @RequestBody Empresa dados, Principal principal) {
        Empresa atual = buscarEmpresaDoUsuario(id, principal);
        boolean trocandoCertificado = dados.getCertificadoDigitalBase64() != null && !dados.getCertificadoDigitalBase64().isBlank();
        if (trocandoCertificado) validarNovoCertificado(dados);
        dados.setId(atual.getId());
        dados.setUsuario(atual.getUsuario());
        dados.setProximoNumeroRps(atual.getProximoNumeroRps());
        dados.setProximoNumeroNfse(atual.getProximoNumeroNfse());
        if (dados.getCertificadoDigitalBase64() == null || dados.getCertificadoDigitalBase64().isBlank()) {
            dados.setCertificadoDigitalBase64(atual.getCertificadoDigitalBase64());
            dados.setCertificadoDigitalNome(atual.getCertificadoDigitalNome());
        }
        if (dados.getSenhaCertificadoDigital() == null || dados.getSenhaCertificadoDigital().isBlank()) {
            dados.setSenhaCertificadoDigital(atual.getSenhaCertificadoDigital());
        }
        if (dados.getEndereco() != null && atual.getEndereco() != null) {
            dados.getEndereco().setId(atual.getEndereco().getId());
        }
        return repository.save(dados);
    }

    private void validarNovoCertificado(Empresa empresa) {
        boolean informouArquivo = empresa.getCertificadoDigitalBase64() != null && !empresa.getCertificadoDigitalBase64().isBlank();
        boolean informouSenha = empresa.getSenhaCertificadoDigital() != null && !empresa.getSenhaCertificadoDigital().isBlank();
        if (informouArquivo || informouSenha) {
            certificateValidator.validarA1(empresa.getCertificadoDigitalBase64(), empresa.getSenhaCertificadoDigital());
        }
    }

    private Empresa buscarEmpresaDoUsuario(Long id, Principal principal) {
        return repository.findByIdAndUsuarioEmailIgnoreCase(id, principal.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Empresa nao encontrada"));
    }
}
