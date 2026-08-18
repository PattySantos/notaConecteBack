package br.com.nfse.nfse_saas.controller;


import br.com.nfse.nfse_saas.domain.Usuario;
import br.com.nfse.nfse_saas.domain.Empresa;
import br.com.nfse.nfse_saas.repository.EmpresaRepository;
import br.com.nfse.nfse_saas.repository.UsuarioRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioRepository repository;
    private final EmpresaRepository empresaRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioController(UsuarioRepository repository, EmpresaRepository empresaRepository,
                             PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.empresaRepository = empresaRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping
    @Transactional
    public Usuario salvar(@RequestBody Usuario usuario) {
        if (usuario.getSenha() == null || usuario.getSenha().isBlank()) {
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.BAD_REQUEST, "Senha obrigatoria");
        }
        if (!usuario.getSenha().startsWith("$2")) {
            usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        }
        List<Empresa> empresasRecebidas = usuario.getEmpresas() == null
                ? Collections.emptyList()
                : usuario.getEmpresas();
        Set<Long> empresasSelecionadas = empresasRecebidas.stream()
                .map(Empresa::getId)
                .filter(id -> id != null)
                .collect(Collectors.toSet());

        usuario.setEmpresas(null);
        Usuario usuarioSalvo = repository.save(usuario);

        List<Empresa> empresasAtuais = empresaRepository.findByUsuarioId(usuarioSalvo.getId());
        empresasAtuais.stream()
                .filter(empresa -> !empresasSelecionadas.contains(empresa.getId()))
                .forEach(empresa -> empresa.setUsuario(null));
        empresaRepository.saveAll(empresasAtuais);

        List<Empresa> empresasVinculadas = empresaRepository.findAllById(empresasSelecionadas);
        empresasVinculadas.forEach(empresa -> empresa.setUsuario(usuarioSalvo));
        empresaRepository.saveAll(empresasVinculadas);

        usuarioSalvo.setEmpresas(empresasVinculadas);
        return usuarioSalvo;
    }
    @GetMapping("/{id}")
    public Usuario buscarPorId(@PathVariable Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id) {
        repository.deleteById(id);
    }
    @GetMapping
    public List<Usuario> listar() {
        return repository.findAll();
    }
}
