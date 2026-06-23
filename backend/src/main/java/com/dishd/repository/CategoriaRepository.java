package com.dishd.repository;

import com.dishd.domain.Categoria;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/** Acesso a dados de {@link Categoria}. */
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    Optional<Categoria> findByNomeIgnoreCase(String nome);

    boolean existsByNomeIgnoreCase(String nome);
}
