package com.dishd.controller;

import com.dishd.dto.UsuarioDTO;
import com.dishd.dto.auth.AuthResponse;
import com.dishd.dto.auth.LoginRequest;
import com.dishd.dto.auth.RegisterRequest;
import com.dishd.security.CurrentUserService;
import com.dishd.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Cadastro, login e dados do usuario autenticado. */
@RestController
@RequestMapping("/api/auth")
@Tag(name = "Autenticacao")
public class AuthController {

    private final AuthService authService;
    private final CurrentUserService currentUserService;

    public AuthController(AuthService authService, CurrentUserService currentUserService) {
        this.authService = authService;
        this.currentUserService = currentUserService;
    }

    @PostMapping("/register")
    @Operation(summary = "Cadastra um novo usuario e retorna o token JWT")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.register(req));
    }

    @PostMapping("/login")
    @Operation(summary = "Autentica por e-mail/senha e retorna o token JWT")
    public AuthResponse login(@Valid @RequestBody LoginRequest req) {
        return authService.login(req);
    }

    @GetMapping("/me")
    @Operation(summary = "Retorna o usuario autenticado (requer token)")
    public UsuarioDTO me() {
        return UsuarioDTO.from(currentUserService.getCurrentUsuario());
    }
}
