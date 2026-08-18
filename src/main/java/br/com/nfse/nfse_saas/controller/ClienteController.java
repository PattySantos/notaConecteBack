package br.com.nfse.nfse_saas.controller;

import br.com.nfse.nfse_saas.domain.Cliente;
import br.com.nfse.nfse_saas.repository.ClienteRepository;
import br.com.nfse.nfse_saas.repository.EmpresaRepository;
import br.com.nfse.nfse_saas.tenant.TenantContext;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

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
        cliente.setId(null);
        cliente.setEmpresa(empresaRepository.getReferenceById(TenantContext.getTenantId()));
        return repository.save(cliente);
    }

    @GetMapping("/{id}")
    public Cliente buscar(@PathVariable Long id) {
        return buscarDoTenant(id);
    }

    @PutMapping("/{id}")
    public Cliente atualizar(@PathVariable Long id, @RequestBody Cliente dados) {
        Cliente cliente = buscarDoTenant(id);
        cliente.setNome(dados.getNome());
        cliente.setDocumento(dados.getDocumento());
        cliente.setTipoPessoa(dados.getTipoPessoa());
        cliente.setNomeFantasia(dados.getNomeFantasia());
        cliente.setInscricaoMunicipal(dados.getInscricaoMunicipal());
        cliente.setTelefone(dados.getTelefone());
        cliente.setEmail(dados.getEmail());
        cliente.setEndereco(dados.getEndereco());
        return repository.save(cliente);
    }

    @GetMapping
    public List<Cliente> listar() {
        return repository.findByEmpresaId(TenantContext.getTenantId());
    }

    private Cliente buscarDoTenant(Long id) {
        return repository.findByIdAndEmpresaId(id, TenantContext.getTenantId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente nao encontrado."));
    }
}
