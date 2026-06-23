package com.dishd.repository;

import com.dishd.domain.Reacao;
import com.dishd.domain.ReacaoAvaliacao;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/** Acesso a dados de {@link ReacaoAvaliacao}. */
public interface ReacaoAvaliacaoRepository extends JpaRepository<ReacaoAvaliacao, Long> {

    Optional<ReacaoAvaliacao> findByUsuario_IdAndAvaliacao_Id(Long usuarioId, Long avaliacaoId);

    long countByAvaliacao_IdAndTipo(Long avaliacaoId, Reacao tipo);

    /** Total de curtidas recebidas em todas as avaliacoes de um usuario. */
    @Query("select count(r) from ReacaoAvaliacao r "
            + "where r.avaliacao.usuario.id = :usuarioId and r.tipo = com.dishd.domain.Reacao.LIKE")
    long countLikesRecebidosByUsuario(@Param("usuarioId") Long usuarioId);
}
