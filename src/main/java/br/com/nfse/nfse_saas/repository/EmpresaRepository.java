package br.com.nfse.nfse_saas.repository;

import br.com.nfse.nfse_saas.domain.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EmpresaRepository extends JpaRepository<Empresa, Long> {

    List<Empresa> findByUsuarioId(Long usuarioId);
    Optional<Empresa> findByIdAndUsuarioEmailIgnoreCase(Long id, String email);
}
