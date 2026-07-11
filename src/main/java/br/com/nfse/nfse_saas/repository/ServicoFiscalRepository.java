package br.com.nfse.nfse_saas.repository;

import br.com.nfse.nfse_saas.domain.ServicoFiscal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ServicoFiscalRepository extends JpaRepository<ServicoFiscal, Long> {

    List<ServicoFiscal> findByAtivoTrueOrderByCodigoServicoNacionalAsc();
}
