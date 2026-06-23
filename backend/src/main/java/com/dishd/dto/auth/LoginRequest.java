package com.dishd.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/** Credenciais de login (autenticacao por e-mail). */
public record LoginRequest(
        @NotBlank @Email String email,
        @NotBlank String password) {
}
