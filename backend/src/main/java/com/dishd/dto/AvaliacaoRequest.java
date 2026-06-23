package com.dishd.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/** Dados para criar/editar uma avaliacao. O autor vem do token JWT. */
public record AvaliacaoRequest(
        @NotNull Long restauranteId,
        @NotNull @DecimalMin("0.0") @DecimalMax("5.0") Double nota,
        @Size(max = 2000) String comentario,
        String fotoUrl,
        boolean favorito) {
}
