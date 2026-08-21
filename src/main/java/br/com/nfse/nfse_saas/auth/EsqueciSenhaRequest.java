package br.com.nfse.nfse_saas.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record EsqueciSenhaRequest(@NotBlank @Email String email) {}
