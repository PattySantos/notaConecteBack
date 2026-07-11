package br.com.nfse.nfse_saas.repository;

import br.com.nfse.nfse_saas.domain.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmpresaRepository extends JpaRepository<Empresa, Long> {

    List<Empresa> findByUsuarioId(Long usuarioId);
}
