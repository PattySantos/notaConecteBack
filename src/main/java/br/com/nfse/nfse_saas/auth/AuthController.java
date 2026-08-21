package br.com.nfse.nfse_saas.auth;

import br.com.nfse.nfse_saas.domain.Usuario;
import br.com.nfse.nfse_saas.repository.UsuarioRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UsuarioRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final PasswordResetService passwordResetService;

    public AuthController(UsuarioRepository repository, PasswordEncoder passwordEncoder, JwtService jwtService,
                          PasswordResetService passwordResetService) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.passwordResetService = passwordResetService;
    }

    @PostMapping("/esqueci-senha")
    public void esqueciSenha(@Valid @RequestBody EsqueciSenhaRequest request) {
        passwordResetService.solicitar(request.email());
    }

    @PostMapping("/redefinir-senha")
    public void redefinirSenha(@Valid @RequestBody RedefinirSenhaRequest request) {
        passwordResetService.redefinir(request.token(), request.senha());
    }

    @PostMapping("/login")
    @Transactional
    public LoginResponse login(@Valid @RequestBody LoginRequest request) {
        Usuario usuario = repository.findByEmailIgnoreCase(request.email().trim())
                .orElseThrow(this::credenciaisInvalidas);
        String senhaSalva = usuario.getSenha();
        boolean senhaValida = senhaSalva != null && (senhaSalva.startsWith("$2")
                ? passwordEncoder.matches(request.senha(), senhaSalva) : senhaSalva.equals(request.senha()));
        if (!senhaValida) throw credenciaisInvalidas();

        if (!senhaSalva.startsWith("$2")) {
            usuario.setSenha(passwordEncoder.encode(request.senha()));
            repository.save(usuario);
        }
        var dados = new LoginResponse.UsuarioAutenticado(
                usuario.getId(), usuario.getNome(), usuario.getEmail(), usuario.getRole());
        return new LoginResponse(jwtService.gerar(usuario.getEmail()), dados);
    }

    private ResponseStatusException credenciaisInvalidas() {
        return new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Email ou senha invalidos");
    }
}
