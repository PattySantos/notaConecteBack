package br.com.nfse.nfse_saas.controller;

import br.com.nfse.nfse_saas.domain.Empresa;
import br.com.nfse.nfse_saas.repository.EmpresaRepository;
import org.springframework.web.bind.annotation.*;

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
}
