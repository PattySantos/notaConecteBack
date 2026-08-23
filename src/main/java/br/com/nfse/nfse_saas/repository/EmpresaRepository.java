package br.com.nfse.nfse_saas.repository;

import br.com.nfse.nfse_saas.domain.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import jakarta.persistence.LockModeType;

import java.util.List;
import java.util.Optional;

public interface EmpresaRepository extends JpaRepository<Empresa, Long> {

    List<Empresa> findByUsuarioId(Long usuarioId);
    List<Empresa> findByUsuarioEmailIgnoreCase(String email);
    Optional<Empresa> findByIdAndUsuarioEmailIgnoreCase(Long id, String email);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select e from Empresa e where e.id = :id")
    Optional<Empresa> findByIdForUpdate(@Param("id") Long id);
}
