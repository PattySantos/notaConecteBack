package br.com.nfse.nfse_saas.controller;

import br.com.nfse.nfse_saas.domain.Produto;
import br.com.nfse.nfse_saas.repository.ProdutoRepository;
import br.com.nfse.nfse_saas.repository.EmpresaRepository;
import br.com.nfse.nfse_saas.tenant.TenantContext;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    private final ProdutoRepository repository;
    private final EmpresaRepository empresaRepository;

    public ProdutoController(ProdutoRepository repository, EmpresaRepository empresaRepository) {
        this.repository = repository;
        this.empresaRepository = empresaRepository;
    }

    @PostMapping
    public Produto salvar(@RequestBody Produto produto) {
        produto.setEmpresa(empresaRepository.getReferenceById(TenantContext.getTenantId()));
        return repository.save(produto);
    }

    @GetMapping
    public List<Produto> listar() {
        return repository.findByEmpresaId(TenantContext.getTenantId());
    }
}
