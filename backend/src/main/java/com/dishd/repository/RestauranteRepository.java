package com.dishd.repository;

import com.dishd.domain.Restaurante;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/** Acesso a dados de {@link Restaurante}. */
public interface RestauranteRepository extends JpaRepository<Restaurante, Long> {

    Page<Restaurante> findByNomeContainingIgnoreCaseOrderByNomeAsc(String nome, Pageable pageable);

    Page<Restaurante> findByCategorias_IdOrderByNomeAsc(Long categoriaId, Pageable pageable);

    Page<Restaurante> findAllByOrderByNomeAsc(Pageable pageable);
}
