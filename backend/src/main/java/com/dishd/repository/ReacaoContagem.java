package com.dishd.repository;

import com.dishd.domain.Reacao;

/** Projecao para contagem de reacoes agrupada por avaliacao e tipo (evita N+1 no feed). */
public interface ReacaoContagem {

    Long getAvaliacaoId();

    Reacao getTipo();

    long getTotal();
}
