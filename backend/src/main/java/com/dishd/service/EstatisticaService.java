package com.dishd.service;

import com.dishd.dto.EstatisticasDTO;
import com.dishd.exception.ResourceNotFoundException;
import com.dishd.repository.AvaliacaoRepository;
import com.dishd.repository.ReacaoAvaliacaoRepository;
import com.dishd.repository.UsuarioRepository;
import java.util.List;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** Calcula estatisticas de consumo de um usuario. */
@Service
public class EstatisticaService {

    private final AvaliacaoRepository avaliacaoRepository;
    private final ReacaoAvaliacaoRepository reacaoRepository;
    private final UsuarioRepository usuarioRepository;

    public EstatisticaService(AvaliacaoRepository avaliacaoRepository,
                              ReacaoAvaliacaoRepository reacaoRepository,
                              UsuarioRepository usuarioRepository) {
        this.avaliacaoRepository = avaliacaoRepository;
        this.reacaoRepository = reacaoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional(readOnly = true)
    public EstatisticasDTO doUsuario(Long usuarioId) {
        if (!usuarioRepository.existsById(usuarioId)) {
            throw ResourceNotFoundException.of("Usuario", usuarioId);
        }
        long total = avaliacaoRepository.countByUsuario_Id(usuarioId);
        double notaMedia = arredondar(avaliacaoRepository.mediaNotaByUsuario(usuarioId));
        long favoritos = avaliacaoRepository.countByUsuario_IdAndFavoritoTrue(usuarioId);
        long restaurantes = avaliacaoRepository.countDistinctRestaurantesByUsuario(usuarioId);
        long likes = reacaoRepository.countLikesRecebidosByUsuario(usuarioId);

        List<String> categorias = avaliacaoRepository
                .categoriasMaisAvaliadasByUsuario(usuarioId, PageRequest.of(0, 1));
        String categoriaFavorita = categorias.isEmpty() ? null : categorias.get(0);

        return new EstatisticasDTO(total, notaMedia, favoritos, restaurantes, likes, categoriaFavorita);
    }

    private double arredondar(double valor) {
        return Math.round(valor * 10.0) / 10.0;
    }
}
