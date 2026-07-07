package com.dishd.repository;

import com.dishd.domain.Reacao;
import com.dishd.domain.ReacaoAvaliacao;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/** Acesso a dados de {@link ReacaoAvaliacao}. */
public interface ReacaoAvaliacaoRepository extends JpaRepository<ReacaoAvaliacao, Long> {

    Optional<ReacaoAvaliacao> findByUsuario_IdAndAvaliacao_Id(Long usuarioId, Long avaliacaoId);

    long countByAvaliacao_IdAndTipo(Long avaliacaoId, Reacao tipo);

    /** Reacoes de um usuario para um conjunto de avaliacoes (batch, evita N+1). */
    List<ReacaoAvaliacao> findByUsuario_IdAndAvaliacao_IdIn(Long usuarioId, Collection<Long> avaliacaoIds);

    /** Contagem de reacoes por (avaliacao, tipo) para um conjunto de avaliacoes (batch). */
    @Query("select r.avaliacao.id as avaliacaoId, r.tipo as tipo, count(r) as total "
            + "from ReacaoAvaliacao r where r.avaliacao.id in :ids "
            + "group by r.avaliacao.id, r.tipo")
    List<ReacaoContagem> contarReacoesPorAvaliacaoIds(@Param("ids") Collection<Long> ids);

    /** Total de curtidas recebidas em todas as avaliacoes de um usuario. */
    @Query("select count(r) from ReacaoAvaliacao r "
            + "where r.avaliacao.usuario.id = :usuarioId and r.tipo = com.dishd.domain.Reacao.LIKE")
    long countLikesRecebidosByUsuario(@Param("usuarioId") Long usuarioId);
}
