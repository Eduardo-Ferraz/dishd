package com.dishd.dto;

import com.dishd.domain.Reacao;
import java.time.Instant;

/**
 * Representacao de uma avaliacao para o feed/diario. Inclui dados resumidos do
 * autor e do restaurante (formato "plano", facil de consumir no frontend),
 * contagem de reacoes e a reacao do usuario logado ({@code minhaReacao}).
 */
public record AvaliacaoDTO(
        Long id,
        Double nota,
        String comentario,
        String fotoUrl,
        boolean favorito,
        Instant criadoEm,
        Long usuarioId,
        String usuarioUsername,
        String usuarioNome,
        Long restauranteId,
        String restauranteNome,
        String restauranteFotoUrl,
        long likes,
        long dislikes,
        Reacao minhaReacao) {
}
