package com.dishd.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.dishd.domain.Avaliacao;
import com.dishd.domain.Restaurante;
import com.dishd.domain.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

/** Testa as agregacoes @Query de {@link AvaliacaoRepository} contra o H2. */
@DataJpaTest
class AvaliacaoRepositoryTest {

    @Autowired
    TestEntityManager em;
    @Autowired
    AvaliacaoRepository repository;

    Usuario usuario;
    Restaurante restaurante;

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

        em.persist(avaliacao(4.0, true));
        em.persist(avaliacao(2.0, false));
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
    void feedOrdenadoPorMaisRecente() {
        var avaliacoes = repository.findByUsuario_IdOrderByCriadoEmDesc(usuario.getId());
        assertThat(avaliacoes).hasSize(2);
    }
}
