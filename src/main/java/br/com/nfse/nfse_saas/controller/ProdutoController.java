package br.com.nfse.nfse_saas.controller;

import br.com.nfse.nfse_saas.domain.Produto;
import br.com.nfse.nfse_saas.repository.ProdutoRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    private final ProdutoRepository repository;

    public ProdutoController(ProdutoRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    public Produto salvar(@RequestBody Produto produto) {
        return repository.save(produto);
    }

    @GetMapping("/empresa/{empresaId}")
    public List<Produto> listarPorEmpresa(@PathVariable Long empresaId) {
        return repository.findByEmpresaId(empresaId);
    }
}
