package com.dishd.dto;

import jakarta.validation.constraints.NotBlank;

/** Dados para criar/editar uma categoria. */
public record CategoriaRequest(@NotBlank String nome) {
}
