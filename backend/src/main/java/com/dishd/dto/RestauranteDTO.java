package com.dishd.dto;

import com.dishd.domain.Restaurante;
import java.util.List;

/** Representacao de um restaurante com suas categorias. */
public record RestauranteDTO(
        Long id,
        String nome,
        String endereco,
        Double notaMedia,
        Integer qntdAvaliacoes,
        String fotoUrl,
        List<CategoriaDTO> categorias) {

    public static RestauranteDTO from(Restaurante r) {
        return new RestauranteDTO(
                r.getId(),
                r.getNome(),
                r.getEndereco(),
                r.getNotaMedia(),
                r.getQntdAvaliacoes(),
                r.getFotoUrl(),
                r.getCategorias().stream().map(CategoriaDTO::from).toList());
    }
}
