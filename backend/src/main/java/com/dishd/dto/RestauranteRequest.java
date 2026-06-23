package com.dishd.dto;

import jakarta.validation.constraints.NotBlank;
import java.util.List;

/** Dados para criar/editar um restaurante. */
public record RestauranteRequest(
        @NotBlank String nome,
        String endereco,
        String fotoUrl,
        List<Long> categoriaIds) {
}
