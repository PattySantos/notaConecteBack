package br.com.nfse.nfse_saas.repository;

import br.com.nfse.nfse_saas.domain.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.Instant;
import java.util.Optional;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    Optional<PasswordResetToken> findByTokenHashAndUsedAtIsNull(String tokenHash);
    boolean existsByUsuarioIdAndCreatedAtAfter(Long usuarioId, Instant instante);

    @Modifying
    @Query("update PasswordResetToken t set t.usedAt = :agora where t.usuario.id = :usuarioId and t.usedAt is null")
    void invalidarTokensDoUsuario(Long usuarioId, Instant agora);
}
