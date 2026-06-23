package com.dishd.service;

import com.dishd.domain.Usuario;
import com.dishd.dto.UsuarioDTO;
import com.dishd.dto.auth.AuthResponse;
import com.dishd.dto.auth.LoginRequest;
import com.dishd.dto.auth.RegisterRequest;
import com.dishd.exception.BadRequestException;
import com.dishd.repository.UsuarioRepository;
import com.dishd.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** Cadastro e autenticacao de usuarios. */
@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder,
                       JwtService jwtService) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Transactional
    public AuthResponse register(RegisterRequest req) {
        if (usuarioRepository.existsByEmail(req.email())) {
            throw new BadRequestException("E-mail ja cadastrado");
        }
        if (usuarioRepository.existsByUsername(req.username())) {
            throw new BadRequestException("Nome de usuario ja em uso");
        }
        Usuario u = new Usuario();
        u.setUsername(req.username());
        u.setNome(req.nome());
        u.setEmail(req.email());
        u.setTelefone(req.telefone());
        u.setPassword(passwordEncoder.encode(req.password()));
        usuarioRepository.save(u);

        String token = jwtService.generateToken(u.getEmail(), u.getId());
        return AuthResponse.bearer(token, UsuarioDTO.from(u));
    }

    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest req) {
        Usuario u = usuarioRepository.findByEmail(req.email())
                .orElseThrow(() -> new BadRequestException("Credenciais invalidas"));
        if (!passwordEncoder.matches(req.password(), u.getPassword())) {
            throw new BadRequestException("Credenciais invalidas");
        }
        String token = jwtService.generateToken(u.getEmail(), u.getId());
        return AuthResponse.bearer(token, UsuarioDTO.from(u));
    }
}
