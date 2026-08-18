package br.com.nfse.nfse_saas.controller;

import br.com.nfse.nfse_saas.domain.Empresa;
import br.com.nfse.nfse_saas.repository.EmpresaRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/empresas")
public class EmpresaController {

    private final EmpresaRepository repository;

    public EmpresaController(EmpresaRepository repository) {
        this.repository = repository;
    }


    @PostMapping
    public Empresa salvar(@RequestBody Empresa empresa) {
        return repository.save(empresa);
    }
    @GetMapping
    public List<Empresa> listar() {
        return repository.findAll();
    }
    @GetMapping("/usuario/{usuarioId}")
    public List<Empresa> listarPorUsuario(@PathVariable Long usuarioId) {
        return repository.findByUsuarioId(usuarioId);
    }

    @GetMapping("/{id}")
    public Empresa buscarPorId(@PathVariable Long id, Principal principal) {
        return buscarEmpresaDoUsuario(id, principal);
    }

    @PutMapping("/{id}")
    public Empresa atualizar(@PathVariable Long id, @RequestBody Empresa dados, Principal principal) {
        Empresa atual = buscarEmpresaDoUsuario(id, principal);
        dados.setId(atual.getId());
        dados.setUsuario(atual.getUsuario());
        if (dados.getEndereco() != null && atual.getEndereco() != null) {
            dados.getEndereco().setId(atual.getEndereco().getId());
        }
        return repository.save(dados);
    }

    private Empresa buscarEmpresaDoUsuario(Long id, Principal principal) {
        return repository.findByIdAndUsuarioEmailIgnoreCase(id, principal.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Empresa nao encontrada"));
    }
}
