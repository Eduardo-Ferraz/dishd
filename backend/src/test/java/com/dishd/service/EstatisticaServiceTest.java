package com.dishd.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import com.dishd.dto.EstatisticasDTO;
import com.dishd.exception.ResourceNotFoundException;
import com.dishd.repository.AvaliacaoRepository;
import com.dishd.repository.ReacaoAvaliacaoRepository;
import com.dishd.repository.UsuarioRepository;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;

/** Testes do calculo de estatisticas do usuario ({@link EstatisticaService}). */
@ExtendWith(MockitoExtension.class)
class EstatisticaServiceTest {

    @Mock
    AvaliacaoRepository avaliacaoRepository;
    @Mock
    ReacaoAvaliacaoRepository reacaoRepository;
    @Mock
    UsuarioRepository usuarioRepository;
    @InjectMocks
    EstatisticaService service;

    @Test
    void doUsuario_inexistente_lancaNotFound() {
        when(usuarioRepository.existsById(1L)).thenReturn(false);

        assertThatThrownBy(() -> service.doUsuario(1L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void doUsuario_agregaEArredondaNota() {
        when(usuarioRepository.existsById(1L)).thenReturn(true);
        when(avaliacaoRepository.countByUsuario_Id(1L)).thenReturn(3L);
        when(avaliacaoRepository.mediaNotaByUsuario(1L)).thenReturn(4.25); // -> 4.3
        when(avaliacaoRepository.countByUsuario_IdAndFavoritoTrue(1L)).thenReturn(2L);
        when(avaliacaoRepository.countDistinctRestaurantesByUsuario(1L)).thenReturn(2L);
        when(reacaoRepository.countLikesRecebidosByUsuario(1L)).thenReturn(5L);
        when(avaliacaoRepository.categoriasMaisAvaliadasByUsuario(eq(1L), any(Pageable.class)))
                .thenReturn(List.of("Italiana"));

        EstatisticasDTO dto = service.doUsuario(1L);

        assertThat(dto.totalAvaliacoes()).isEqualTo(3);
        assertThat(dto.notaMedia()).isEqualTo(4.3);
        assertThat(dto.totalFavoritos()).isEqualTo(2);
        assertThat(dto.totalRestaurantesVisitados()).isEqualTo(2);
        assertThat(dto.totalLikesRecebidos()).isEqualTo(5);
        assertThat(dto.categoriaFavorita()).isEqualTo("Italiana");
    }

    @Test
    void doUsuario_semCategorias_categoriaFavoritaNula() {
        when(usuarioRepository.existsById(1L)).thenReturn(true);
        when(avaliacaoRepository.countByUsuario_Id(1L)).thenReturn(0L);
        when(avaliacaoRepository.mediaNotaByUsuario(1L)).thenReturn(0.0);
        when(avaliacaoRepository.countByUsuario_IdAndFavoritoTrue(1L)).thenReturn(0L);
        when(avaliacaoRepository.countDistinctRestaurantesByUsuario(1L)).thenReturn(0L);
        when(reacaoRepository.countLikesRecebidosByUsuario(1L)).thenReturn(0L);
        when(avaliacaoRepository.categoriasMaisAvaliadasByUsuario(eq(1L), any(Pageable.class)))
                .thenReturn(List.of());

        EstatisticasDTO dto = service.doUsuario(1L);

        assertThat(dto.categoriaFavorita()).isNull();
    }
}
