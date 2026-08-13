package br.com.nfse.nfse_saas.controller;

import br.com.nfse.nfse_saas.domain.Cliente;
import br.com.nfse.nfse_saas.repository.ClienteRepository;
import br.com.nfse.nfse_saas.repository.EmpresaRepository;
import br.com.nfse.nfse_saas.tenant.TenantContext;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    private final ClienteRepository repository;
    private final EmpresaRepository empresaRepository;

    public ClienteController(ClienteRepository repository, EmpresaRepository empresaRepository) {
        this.repository = repository;
        this.empresaRepository = empresaRepository;
    }

    @PostMapping
    public Cliente salvar(@RequestBody Cliente cliente) {
        cliente.setEmpresa(empresaRepository.getReferenceById(TenantContext.getTenantId()));
        return repository.save(cliente);
    }

    @GetMapping
    public List<Cliente> listar() {
        return repository.findByEmpresaId(TenantContext.getTenantId());
    }
}
