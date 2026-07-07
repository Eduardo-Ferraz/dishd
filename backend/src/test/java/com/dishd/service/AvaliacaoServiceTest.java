package com.dishd.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.dishd.domain.Avaliacao;
import com.dishd.domain.Reacao;
import com.dishd.domain.ReacaoAvaliacao;
import com.dishd.domain.Restaurante;
import com.dishd.domain.Usuario;
import com.dishd.dto.AvaliacaoDTO;
import com.dishd.dto.AvaliacaoRequest;
import com.dishd.dto.ReacaoRequest;
import com.dishd.exception.ForbiddenException;
import com.dishd.exception.ResourceNotFoundException;
import com.dishd.repository.AvaliacaoRepository;
import com.dishd.repository.ReacaoAvaliacaoRepository;
import com.dishd.repository.RestauranteRepository;
import com.dishd.security.CurrentUserService;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/** Testes das regras de avaliacoes, ownership e reacoes ({@link AvaliacaoService}). */
@ExtendWith(MockitoExtension.class)
class AvaliacaoServiceTest {

    @Mock
    AvaliacaoRepository avaliacaoRepository;
    @Mock
    RestauranteRepository restauranteRepository;
    @Mock
    ReacaoAvaliacaoRepository reacaoRepository;
    @Mock
    CurrentUserService currentUserService;
    @InjectMocks
    AvaliacaoService service;

    Usuario autor;
    Restaurante restaurante;

    @BeforeEach
    void setUp() {
        autor = new Usuario();
        autor.setId(1L);
        autor.setUsername("autor");
        autor.setNome("Autor");

        restaurante = new Restaurante();
        restaurante.setId(10L);
        restaurante.setNome("Restaurante");

        // Stubs comuns usados por toDTO / recalcularEstatisticas (lenient: nem todo teste chega la).
        lenient().when(currentUserService.getCurrentUsuario()).thenReturn(autor);
        lenient().when(currentUserService.getCurrentUsuarioIdOrNull()).thenReturn(1L);
        lenient().when(reacaoRepository.countByAvaliacao_IdAndTipo(nullable(Long.class), any()))
                .thenReturn(0L);
        lenient().when(reacaoRepository.findByUsuario_IdAndAvaliacao_Id(nullable(Long.class), nullable(Long.class)))
                .thenReturn(Optional.empty());
        lenient().when(avaliacaoRepository.mediaNotaByRestaurante(anyLong())).thenReturn(4.0);
        lenient().when(avaliacaoRepository.countByRestaurante_Id(anyLong())).thenReturn(1L);
    }

    private Avaliacao avaliacaoDe(Usuario dono) {
        Avaliacao a = new Avaliacao();
        a.setId(5L);
        a.setNota(3.0);
        a.setUsuario(dono);
        a.setRestaurante(restaurante);
        return a;
    }

    @Test
    void criar_salvaERecalculaEstatisticas() {
        AvaliacaoRequest req = new AvaliacaoRequest(10L, 4.0, "bom", null, false);
        when(restauranteRepository.findById(10L)).thenReturn(Optional.of(restaurante));

        AvaliacaoDTO dto = service.criar(req);

        assertThat(dto.nota()).isEqualTo(4.0);
        assertThat(dto.restauranteId()).isEqualTo(10L);
        assertThat(dto.usuarioId()).isEqualTo(1L);
        verify(avaliacaoRepository).save(any(Avaliacao.class));
        // recalculo: media 4.0, 1 avaliacao -> persistido no restaurante.
        verify(restauranteRepository).save(restaurante);
        assertThat(restaurante.getNotaMedia()).isEqualTo(4.0);
        assertThat(restaurante.getQntdAvaliacoes()).isEqualTo(1);
    }

    @Test
    void criar_restauranteInexistente_lancaNotFound() {
        AvaliacaoRequest req = new AvaliacaoRequest(99L, 4.0, "bom", null, false);
        when(restauranteRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.criar(req))
                .isInstanceOf(ResourceNotFoundException.class);
        verify(avaliacaoRepository, never()).save(any());
    }

    @Test
    void atualizar_deOutroUsuario_lancaForbidden() {
        Usuario outro = new Usuario();
        outro.setId(2L);
        Avaliacao alheia = avaliacaoDe(outro);
        when(avaliacaoRepository.findById(5L)).thenReturn(Optional.of(alheia));
        AvaliacaoRequest req = new AvaliacaoRequest(10L, 5.0, "editado", null, true);

        assertThatThrownBy(() -> service.atualizar(5L, req))
                .isInstanceOf(ForbiddenException.class);
        verify(avaliacaoRepository, never()).save(any());
    }

    @Test
    void excluir_deOutroUsuario_lancaForbidden() {
        Usuario outro = new Usuario();
        outro.setId(2L);
        Avaliacao alheia = avaliacaoDe(outro);
        when(avaliacaoRepository.findById(5L)).thenReturn(Optional.of(alheia));

        assertThatThrownBy(() -> service.excluir(5L))
                .isInstanceOf(ForbiddenException.class);
        verify(avaliacaoRepository, never()).delete(any());
    }

    @Test
    void excluir_propria_removeERecalcula() {
        Avaliacao minha = avaliacaoDe(autor);
        when(avaliacaoRepository.findById(5L)).thenReturn(Optional.of(minha));

        service.excluir(5L);

        verify(avaliacaoRepository).delete(minha);
        verify(restauranteRepository).save(restaurante);
    }

    @Test
    void reagir_semReacaoPrevia_criaNova() {
        Avaliacao a = avaliacaoDe(autor);
        when(avaliacaoRepository.findById(5L)).thenReturn(Optional.of(a));

        service.reagir(5L, new ReacaoRequest(Reacao.LIKE));

        ArgumentCaptor<ReacaoAvaliacao> captor = ArgumentCaptor.forClass(ReacaoAvaliacao.class);
        verify(reacaoRepository).save(captor.capture());
        assertThat(captor.getValue().getTipo()).isEqualTo(Reacao.LIKE);
        assertThat(captor.getValue().getUsuario()).isEqualTo(autor);
        assertThat(captor.getValue().getAvaliacao()).isEqualTo(a);
    }

    @Test
    void reagir_comReacaoExistente_atualizaTipo() {
        Avaliacao a = avaliacaoDe(autor);
        when(avaliacaoRepository.findById(5L)).thenReturn(Optional.of(a));
        ReacaoAvaliacao existente = new ReacaoAvaliacao();
        existente.setUsuario(autor);
        existente.setAvaliacao(a);
        existente.setTipo(Reacao.DISLIKE);
        when(reacaoRepository.findByUsuario_IdAndAvaliacao_Id(1L, 5L)).thenReturn(Optional.of(existente));

        service.reagir(5L, new ReacaoRequest(Reacao.LIKE));

        assertThat(existente.getTipo()).isEqualTo(Reacao.LIKE);
        verify(reacaoRepository).save(existente);
    }

    @Test
    void removerReacao_quandoExiste_deleta() {
        Avaliacao a = avaliacaoDe(autor);
        when(avaliacaoRepository.findById(5L)).thenReturn(Optional.of(a));
        ReacaoAvaliacao existente = new ReacaoAvaliacao();
        existente.setUsuario(autor);
        existente.setAvaliacao(a);
        existente.setTipo(Reacao.LIKE);
        when(reacaoRepository.findByUsuario_IdAndAvaliacao_Id(1L, 5L)).thenReturn(Optional.of(existente));

        service.removerReacao(5L);

        verify(reacaoRepository).delete(existente);
    }

    @Test
    void buscar_avaliacaoInexistente_lancaNotFound() {
        when(avaliacaoRepository.findById(404L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.buscar(404L))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}
