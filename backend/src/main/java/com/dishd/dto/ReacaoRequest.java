package com.dishd.dto;

import com.dishd.domain.Reacao;
import jakarta.validation.constraints.NotNull;

/** Dados para reagir a uma avaliacao (LIKE ou DISLIKE). */
public record ReacaoRequest(@NotNull Reacao tipo) {
}
