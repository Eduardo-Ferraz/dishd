package com.dishd.dto;

/** Estatisticas de consumo de um usuario. */
public record EstatisticasDTO(
        long totalAvaliacoes,
        double notaMedia,
        long totalFavoritos,
        long totalRestaurantesVisitados,
        long totalLikesRecebidos,
        String categoriaFavorita) {
}
