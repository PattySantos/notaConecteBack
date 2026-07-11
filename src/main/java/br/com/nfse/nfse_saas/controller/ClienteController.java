package br.com.nfse.nfse_saas.controller;

import br.com.nfse.nfse_saas.domain.Cliente;
import br.com.nfse.nfse_saas.repository.ClienteRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    private final ClienteRepository repository;

    public ClienteController(ClienteRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    public Cliente salvar(@RequestBody Cliente cliente) {
        return repository.save(cliente);
    }

    @GetMapping("/empresa/{empresaId}")
    public List<Cliente> listarPorEmpresa(@PathVariable Long empresaId) {
        return repository.findByEmpresaId(empresaId);
    }
}