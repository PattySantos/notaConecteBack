package br.com.nfse.nfse_saas.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RedefinirSenhaRequest(@NotBlank String token, @NotBlank @Size(min = 8) String senha) {}
