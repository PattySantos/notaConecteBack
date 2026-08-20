package br.com.nfse.nfse_saas.controller;

import br.com.nfse.nfse_saas.domain.Produto;
import br.com.nfse.nfse_saas.repository.ProdutoRepository;
import br.com.nfse.nfse_saas.repository.EmpresaRepository;
import br.com.nfse.nfse_saas.tenant.TenantContext;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

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
        produto.setId(null);
        produto.setEmpresa(empresaRepository.getReferenceById(TenantContext.getTenantId()));
        return repository.save(produto);
    }

    @GetMapping("/{id}")
    public Produto buscar(@PathVariable Long id) {
        return buscarDoTenant(id);
    }

    @PutMapping("/{id}")
    public Produto atualizar(@PathVariable Long id, @RequestBody Produto dados) {
        Produto produto = buscarDoTenant(id);
        produto.setNome(dados.getNome());
        produto.setCodigo(dados.getCodigo());
        produto.setValor(dados.getValor());
        produto.setDescricao(dados.getDescricao());
        produto.setCodigoServicoNacional(dados.getCodigoServicoNacional());
        produto.setCodigoServicoMunicipal(dados.getCodigoServicoMunicipal());
        produto.setCodigoCnae(dados.getCodigoCnae());
        produto.setCodigoNbs(dados.getCodigoNbs());
        produto.setAliquotaIss(dados.getAliquotaIss());
        produto.setIssRetido(dados.getIssRetido());
        produto.setExigibilidadeIss(dados.getExigibilidadeIss());
        produto.setMunicipioIncidenciaIbge(dados.getMunicipioIncidenciaIbge());
        produto.setIndicadorIbscbs(dados.getIndicadorIbscbs());
        produto.setAtivo(dados.getAtivo());
        return repository.save(produto);
    }

    @GetMapping
    public List<Produto> listar() {
        return repository.findByEmpresaId(TenantContext.getTenantId());
    }

    private Produto buscarDoTenant(Long id) {
        return repository.findByIdAndEmpresaId(id, TenantContext.getTenantId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Servico nao encontrado."));
    }
}
