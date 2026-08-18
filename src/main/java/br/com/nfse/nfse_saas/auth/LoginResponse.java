package br.com.nfse.nfse_saas.auth;

public record LoginResponse(String token, UsuarioAutenticado usuario) {
    public record UsuarioAutenticado(Long id, String nome, String email, String role) {}
}
