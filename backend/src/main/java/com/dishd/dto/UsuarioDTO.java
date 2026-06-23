package com.dishd.dto;

import com.dishd.domain.Usuario;

/** Representacao publica de um usuario (sem a senha). */
public record UsuarioDTO(Long id, String username, String nome, String email, String telefone) {

    public static UsuarioDTO from(Usuario u) {
        return new UsuarioDTO(u.getId(), u.getUsername(), u.getNome(), u.getEmail(), u.getTelefone());
    }
}
