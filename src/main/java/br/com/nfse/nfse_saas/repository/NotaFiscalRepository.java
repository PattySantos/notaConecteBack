package br.com.nfse.nfse_saas.repository;


import br.com.nfse.nfse_saas.domain.NotaFiscal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import jakarta.persistence.LockModeType;

import java.util.List;
import java.util.Optional;

public interface NotaFiscalRepository extends JpaRepository<NotaFiscal, Long> {

    List<NotaFiscal> findByEmpresaId(Long empresaId);
    Optional<NotaFiscal> findByIdAndEmpresaId(Long id, Long empresaId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select n from NotaFiscal n where n.id = :id")
    Optional<NotaFiscal> findByIdForUpdate(@Param("id") Long id);
}
