package br.com.nfse.nfse_saas.controller;

import br.com.nfse.nfse_saas.domain.ServicoFiscal;
import br.com.nfse.nfse_saas.repository.ServicoFiscalRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/servicos-fiscais")
public class ServicoFiscalController {

    private final ServicoFiscalRepository repository;

    public ServicoFiscalController(ServicoFiscalRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    public ServicoFiscal salvar(@RequestBody ServicoFiscal servicoFiscal) {
        return repository.save(servicoFiscal);
    }

    @GetMapping
    public List<ServicoFiscal> listar(@RequestParam(required = false) Boolean somenteAtivos) {
        if (Boolean.TRUE.equals(somenteAtivos)) {
            return repository.findByAtivoTrueOrderByCodigoServicoNacionalAsc();
        }

        return repository.findAll();
    }

    @GetMapping("/{id}")
    public ServicoFiscal buscarPorId(@PathVariable Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Servico fiscal nao encontrado"));
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id) {
        repository.deleteById(id);
    }
}
