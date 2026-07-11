package br.com.nfse.nfse_saas.repository;

import br.com.nfse.nfse_saas.domain.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    List<Produto> findByEmpresaId(Long empresaId);
}
