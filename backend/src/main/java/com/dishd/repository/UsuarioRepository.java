package com.dishd.repository;

import com.dishd.domain.Usuario;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/** Acesso a dados de {@link Usuario}. */
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByEmail(String email);

    Optional<Usuario> findByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);
}
