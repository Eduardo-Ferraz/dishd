package com.dishd.dto.auth;

import com.dishd.dto.UsuarioDTO;

/** Resposta de login/cadastro: token JWT + dados do usuario autenticado. */
public record AuthResponse(String token, String tipo, UsuarioDTO usuario) {

    public static AuthResponse bearer(String token, UsuarioDTO usuario) {
        return new AuthResponse(token, "Bearer", usuario);
    }
}
