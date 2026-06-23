package com.dishd.repository;

import com.dishd.domain.Avaliacao;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/** Acesso a dados de {@link Avaliacao}. */
public interface AvaliacaoRepository extends JpaRepository<Avaliacao, Long> {

    /** Feed social: avaliacoes mais recentes primeiro. */
    Page<Avaliacao> findAllByOrderByCriadoEmDesc(Pageable pageable);

    /** Diario de um usuario. */
    List<Avaliacao> findByUsuario_IdOrderByCriadoEmDesc(Long usuarioId);

    /** Avaliacoes de um restaurante. */
    List<Avaliacao> findByRestaurante_IdOrderByCriadoEmDesc(Long restauranteId);

    // ---- agregacoes para o restaurante ----

    @Query("select coalesce(avg(a.nota), 0) from Avaliacao a where a.restaurante.id = :restauranteId")
    double mediaNotaByRestaurante(@Param("restauranteId") Long restauranteId);

    long countByRestaurante_Id(Long restauranteId);

    // ---- agregacoes para estatisticas do usuario ----

    long countByUsuario_Id(Long usuarioId);

    long countByUsuario_IdAndFavoritoTrue(Long usuarioId);

    @Query("select coalesce(avg(a.nota), 0) from Avaliacao a where a.usuario.id = :usuarioId")
    double mediaNotaByUsuario(@Param("usuarioId") Long usuarioId);

    @Query("select count(distinct a.restaurante.id) from Avaliacao a where a.usuario.id = :usuarioId")
    long countDistinctRestaurantesByUsuario(@Param("usuarioId") Long usuarioId);

    @Query("select c.nome from Avaliacao a join a.restaurante.categorias c "
            + "where a.usuario.id = :usuarioId group by c.nome order by count(c) desc")
    List<String> categoriasMaisAvaliadasByUsuario(@Param("usuarioId") Long usuarioId, Pageable pageable);
}
