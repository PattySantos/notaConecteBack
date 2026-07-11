package br.com.nfse.nfse_saas.repository;


import br.com.nfse.nfse_saas.domain.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
}
