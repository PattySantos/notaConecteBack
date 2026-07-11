package br.com.nfse.nfse_saas.repository;

import br.com.nfse.nfse_saas.domain.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    List<Cliente> findByEmpresaId(Long empresaId);
}
