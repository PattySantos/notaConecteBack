package br.com.nfse.nfse_saas.auth;

import br.com.nfse.nfse_saas.domain.PasswordResetToken;
import br.com.nfse.nfse_saas.domain.Usuario;
import br.com.nfse.nfse_saas.repository.PasswordResetTokenRepository;
import br.com.nfse.nfse_saas.repository.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;

@Service
public class PasswordResetService {
    private static final Logger log = LoggerFactory.getLogger(PasswordResetService.class);
    private static final SecureRandom RANDOM = new SecureRandom();

    private final UsuarioRepository usuarioRepository;
    private final PasswordResetTokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender mailSender;
    private final long expirationMinutes;
    private final String frontendUrl;
    private final boolean mailEnabled;
    private final String mailFrom;

    public PasswordResetService(UsuarioRepository usuarioRepository,
                                PasswordResetTokenRepository tokenRepository,
                                PasswordEncoder passwordEncoder,
                                JavaMailSender mailSender,
                                @Value("${auth.password-reset.expiration-minutes:30}") long expirationMinutes,
                                @Value("${auth.password-reset.frontend-url:http://localhost:4200}") String frontendUrl,
                                @Value("${auth.password-reset.mail-enabled:false}") boolean mailEnabled,
                                @Value("${auth.password-reset.mail-from:nao-responda@notaconecte.com.br}") String mailFrom) {
        this.usuarioRepository = usuarioRepository;
        this.tokenRepository = tokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.mailSender = mailSender;
        this.expirationMinutes = expirationMinutes;
        this.frontendUrl = frontendUrl;
        this.mailEnabled = mailEnabled;
        this.mailFrom = mailFrom;
    }

    @Transactional
    public void solicitar(String email) {
        usuarioRepository.findByEmailIgnoreCase(email.trim()).ifPresent(this::criarEEnviarToken);
    }

    private void criarEEnviarToken(Usuario usuario) {
        Instant agora = Instant.now();
        if (tokenRepository.existsByUsuarioIdAndCreatedAtAfter(usuario.getId(), agora.minus(1, ChronoUnit.MINUTES))) {
            return;
        }
        tokenRepository.invalidarTokensDoUsuario(usuario.getId(), agora);
        String token = gerarToken();
        PasswordResetToken entidade = new PasswordResetToken();
        entidade.setUsuario(usuario);
        entidade.setTokenHash(hash(token));
        entidade.setCreatedAt(agora);
        entidade.setExpiresAt(agora.plus(expirationMinutes, ChronoUnit.MINUTES));
        tokenRepository.save(entidade);

        String link = frontendUrl.replaceAll("/$", "") + "/redefinir-senha?token=" + token;
        if (!mailEnabled) {
            log.warn("Envio de e-mail desabilitado. Link de redefinicao para ambiente local: {}", link);
            return;
        }
        SimpleMailMessage mensagem = new SimpleMailMessage();
        mensagem.setFrom(mailFrom);
        mensagem.setTo(usuario.getEmail());
        mensagem.setSubject("Redefinição de senha - Nota Conecte");
        mensagem.setText("Olá, " + usuario.getNome() + ".\n\nUse o link abaixo para redefinir sua senha. "
                + "Ele expira em " + expirationMinutes + " minutos e pode ser utilizado apenas uma vez.\n\n"
                + link + "\n\nSe você não solicitou a alteração, ignore esta mensagem.");
        try {
            mailSender.send(mensagem);
        } catch (MailException exception) {
            log.error("Falha ao enviar e-mail de redefinicao de senha", exception);
        }
    }

    @Transactional
    public void redefinir(String token, String novaSenha) {
        Instant agora = Instant.now();
        PasswordResetToken entidade = tokenRepository.findByTokenHashAndUsedAtIsNull(hash(token.trim()))
                .filter(item -> item.getExpiresAt().isAfter(agora))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Token invalido ou expirado"));
        Usuario usuario = entidade.getUsuario();
        usuario.setSenha(passwordEncoder.encode(novaSenha));
        usuarioRepository.save(usuario);
        tokenRepository.invalidarTokensDoUsuario(usuario.getId(), agora);
    }

    private String gerarToken() {
        byte[] bytes = new byte[32];
        RANDOM.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    private String hash(String token) {
        try {
            byte[] digest = MessageDigest.getInstance("SHA-256").digest(token.getBytes(StandardCharsets.UTF_8));
            return java.util.HexFormat.of().formatHex(digest);
        } catch (NoSuchAlgorithmException exception) {
            throw new IllegalStateException("SHA-256 indisponivel", exception);
        }
    }
}
