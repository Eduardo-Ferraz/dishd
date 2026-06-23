package com.dishd.repository;

import com.dishd.domain.Restaurante;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/** Acesso a dados de {@link Restaurante}. */
public interface RestauranteRepository extends JpaRepository<Restaurante, Long> {

    List<Restaurante> findByNomeContainingIgnoreCaseOrderByNomeAsc(String nome);

    List<Restaurante> findByCategorias_IdOrderByNomeAsc(Long categoriaId);

    List<Restaurante> findAllByOrderByNomeAsc();
}
