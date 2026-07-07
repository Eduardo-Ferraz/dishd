package com.dishd.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.dishd.domain.Usuario;
import com.dishd.dto.auth.AuthResponse;
import com.dishd.dto.auth.LoginRequest;
import com.dishd.dto.auth.RegisterRequest;
import com.dishd.exception.BadRequestException;
import com.dishd.repository.UsuarioRepository;
import com.dishd.security.JwtService;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

/** Testes de cadastro e login ({@link AuthService}). */
@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    UsuarioRepository usuarioRepository;
    @Mock
    PasswordEncoder passwordEncoder;
    @Mock
    JwtService jwtService;
    @InjectMocks
    AuthService authService;

    @Test
    void register_novoUsuario_retornaTokenBearer() {
        RegisterRequest req = new RegisterRequest("joao", "Joao", "joao@x.com", "27999", "segredo123");
        when(usuarioRepository.existsByEmail("joao@x.com")).thenReturn(false);
        when(usuarioRepository.existsByUsername("joao")).thenReturn(false);
        when(passwordEncoder.encode("segredo123")).thenReturn("hash");
        when(jwtService.generateToken(any(), any())).thenReturn("jwt");

        AuthResponse res = authService.register(req);

        assertThat(res.token()).isEqualTo("jwt");
        assertThat(res.tipo()).isEqualTo("Bearer");
        assertThat(res.usuario().email()).isEqualTo("joao@x.com");
        assertThat(res.usuario().username()).isEqualTo("joao");
        verify(usuarioRepository).save(any(Usuario.class));
    }

    @Test
    void register_emailDuplicado_lancaBadRequestSemSalvar() {
        RegisterRequest req = new RegisterRequest("joao", "Joao", "joao@x.com", null, "segredo123");
        when(usuarioRepository.existsByEmail("joao@x.com")).thenReturn(true);

        assertThatThrownBy(() -> authService.register(req))
                .isInstanceOf(BadRequestException.class);
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void register_usernameDuplicado_lancaBadRequest() {
        RegisterRequest req = new RegisterRequest("joao", "Joao", "joao@x.com", null, "segredo123");
        when(usuarioRepository.existsByEmail("joao@x.com")).thenReturn(false);
        when(usuarioRepository.existsByUsername("joao")).thenReturn(true);

        assertThatThrownBy(() -> authService.register(req))
                .isInstanceOf(BadRequestException.class);
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void login_credenciaisValidas_retornaToken() {
        Usuario u = new Usuario();
        u.setEmail("demo@x.com");
        u.setUsername("demo");
        u.setNome("Demo");
        u.setPassword("hash");
        LoginRequest req = new LoginRequest("demo@x.com", "senha");
        when(usuarioRepository.findByEmail("demo@x.com")).thenReturn(Optional.of(u));
        when(passwordEncoder.matches("senha", "hash")).thenReturn(true);
        when(jwtService.generateToken(any(), any())).thenReturn("jwt");

        AuthResponse res = authService.login(req);

        assertThat(res.token()).isEqualTo("jwt");
        assertThat(res.usuario().email()).isEqualTo("demo@x.com");
    }

    @Test
    void login_senhaIncorreta_lancaBadRequest() {
        Usuario u = new Usuario();
        u.setPassword("hash");
        LoginRequest req = new LoginRequest("demo@x.com", "errada");
        when(usuarioRepository.findByEmail("demo@x.com")).thenReturn(Optional.of(u));
        when(passwordEncoder.matches("errada", "hash")).thenReturn(false);

        assertThatThrownBy(() -> authService.login(req))
                .isInstanceOf(BadRequestException.class);
    }

    @Test
    void login_emailInexistente_lancaBadRequest() {
        LoginRequest req = new LoginRequest("nao@x.com", "senha");
        when(usuarioRepository.findByEmail("nao@x.com")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> authService.login(req))
                .isInstanceOf(BadRequestException.class);
    }
}
