package br.com.nfse.nfse_saas.repository;


import br.com.nfse.nfse_saas.domain.NotaFiscal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NotaFiscalRepository extends JpaRepository<NotaFiscal, Long> {

    List<NotaFiscal> findByEmpresaId(Long empresaId);
    Optional<NotaFiscal> findByIdAndEmpresaId(Long id, Long empresaId);
}
