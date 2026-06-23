package com.dishd.dto;

import com.dishd.domain.Categoria;

/** Representacao de uma categoria. */
public record CategoriaDTO(Long id, String nome) {

    public static CategoriaDTO from(Categoria c) {
        return new CategoriaDTO(c.getId(), c.getNome());
    }
}
