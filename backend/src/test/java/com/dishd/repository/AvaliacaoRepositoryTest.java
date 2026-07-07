package com.dishd.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.dishd.domain.Avaliacao;
import com.dishd.domain.Reacao;
import com.dishd.domain.ReacaoAvaliacao;
import com.dishd.domain.Restaurante;
import com.dishd.domain.Usuario;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;

/** Testa as agregacoes/batch @Query dos repositorios de avaliacao e reacao contra o H2. */
@DataJpaTest
class AvaliacaoRepositoryTest {

    @Autowired
    TestEntityManager em;
    @Autowired
    AvaliacaoRepository repository;
    @Autowired
    ReacaoAvaliacaoRepository reacaoRepository;

    Usuario usuario;
    Restaurante restaurante;
    Avaliacao a1;
    Avaliacao a2;

    @BeforeEach
    void seed() {
        usuario = new Usuario();
        usuario.setUsername("u");
        usuario.setNome("Usuario");
        usuario.setEmail("u@x.com");
        usuario.setPassword("hash");
        em.persist(usuario);

        restaurante = new Restaurante();
        restaurante.setNome("Restaurante");
        em.persist(restaurante);

        a1 = avaliacao(4.0, true);
        a2 = avaliacao(2.0, false);
        em.persist(a1);
        em.persist(a2);
        em.flush();
    }

    private Avaliacao avaliacao(double nota, boolean favorito) {
        Avaliacao a = new Avaliacao();
        a.setNota(nota);
        a.setFavorito(favorito);
        a.setUsuario(usuario);
        a.setRestaurante(restaurante);
        return a;
    }

    @Test
    void contaEMediaPorRestaurante() {
        assertThat(repository.countByRestaurante_Id(restaurante.getId())).isEqualTo(2);
        assertThat(repository.mediaNotaByRestaurante(restaurante.getId())).isEqualTo(3.0);
    }

    @Test
    void mediaPorRestauranteSemAvaliacoes_retornaZero() {
        assertThat(repository.mediaNotaByRestaurante(999L)).isEqualTo(0.0);
    }

    @Test
    void estatisticasPorUsuario() {
        Long uid = usuario.getId();
        assertThat(repository.countByUsuario_Id(uid)).isEqualTo(2);
        assertThat(repository.countByUsuario_IdAndFavoritoTrue(uid)).isEqualTo(1);
        assertThat(repository.mediaNotaByUsuario(uid)).isEqualTo(3.0);
        assertThat(repository.countDistinctRestaurantesByUsuario(uid)).isEqualTo(1);
    }

    @Test
    void diarioDoUsuario_paginado() {
        var pagina = repository.findByUsuario_IdOrderByCriadoEmDesc(usuario.getId(), PageRequest.of(0, 10));
        assertThat(pagina.getTotalElements()).isEqualTo(2);
        assertThat(pagina.getContent()).hasSize(2);
    }

    @Test
    void contagemDeReacoesEmLote_agrupaPorAvaliacaoETipo() {
        // a1: 1 like ; a2: 1 dislike (usuario reage nas proprias, so para exercitar a query).
        reagir(a1, Reacao.LIKE);
        reagir(a2, Reacao.DISLIKE);
        em.flush();

        List<ReacaoContagem> contagens =
                reacaoRepository.contarReacoesPorAvaliacaoIds(List.of(a1.getId(), a2.getId()));

        assertThat(contagens).hasSize(2);
        assertThat(contagens).anySatisfy(c -> {
            assertThat(c.getAvaliacaoId()).isEqualTo(a1.getId());
            assertThat(c.getTipo()).isEqualTo(Reacao.LIKE);
            assertThat(c.getTotal()).isEqualTo(1);
        });
        assertThat(contagens).anySatisfy(c -> {
            assertThat(c.getAvaliacaoId()).isEqualTo(a2.getId());
            assertThat(c.getTipo()).isEqualTo(Reacao.DISLIKE);
            assertThat(c.getTotal()).isEqualTo(1);
        });
    }

    @Test
    void reacoesDoUsuarioEmLote_retornaSomenteDoConjunto() {
        reagir(a1, Reacao.LIKE);
        em.flush();

        List<ReacaoAvaliacao> minhas = reacaoRepository
                .findByUsuario_IdAndAvaliacao_IdIn(usuario.getId(), List.of(a1.getId(), a2.getId()));

        assertThat(minhas).hasSize(1);
        assertThat(minhas.get(0).getAvaliacao().getId()).isEqualTo(a1.getId());
        assertThat(minhas.get(0).getTipo()).isEqualTo(Reacao.LIKE);
    }

    private void reagir(Avaliacao a, Reacao tipo) {
        ReacaoAvaliacao r = new ReacaoAvaliacao();
        r.setUsuario(usuario);
        r.setAvaliacao(a);
        r.setTipo(tipo);
        em.persist(r);
    }
}
