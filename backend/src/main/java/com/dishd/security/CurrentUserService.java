package com.dishd.security;

import com.dishd.domain.Usuario;
import com.dishd.exception.ForbiddenException;
import com.dishd.repository.UsuarioRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/** Recupera o usuario autenticado a partir do {@code SecurityContext}. */
@Service
public class CurrentUserService {

    private final UsuarioRepository usuarioRepository;

    public CurrentUserService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    /** Usuario logado; lanca {@link ForbiddenException} se nao houver autenticacao. */
    public Usuario getCurrentUsuario() {
        String email = getAuthenticatedEmail();
        if (email == null) {
            throw new ForbiddenException("Autenticacao necessaria");
        }
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ForbiddenException("Usuario autenticado nao encontrado"));
    }

    /** Id do usuario logado, ou {@code null} se a requisicao for anonima. */
    public Long getCurrentUsuarioIdOrNull() {
        String email = getAuthenticatedEmail();
        if (email == null) {
            return null;
        }
        return usuarioRepository.findByEmail(email).map(Usuario::getId).orElse(null);
    }

    private String getAuthenticatedEmail() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
            return null;
        }
        return auth.getName();
    }
}
