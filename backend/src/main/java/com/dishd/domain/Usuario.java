package com.dishd.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.annotations.CreationTimestamp;

/**
 * Usuario cadastrado na plataforma. Faz avaliacoes de restaurantes e reage
 * (curte / descurte) avaliacoes de outros usuarios.
 */
@Entity
@Table(name = "usuario", uniqueConstraints = {
        @UniqueConstraint(name = "uk_usuario_email", columnNames = "email"),
        @UniqueConstraint(name = "uk_usuario_username", columnNames = "username")
})
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Identificador publico unico (ex.: "@joao"). */
    @Column(nullable = false, length = 50)
    private String username;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String email;

    private String telefone;

    /** Senha em formato hash (BCrypt). Nunca exposta em DTOs. */
    @Column(nullable = false)
    private String password;

    @CreationTimestamp
    @Column(updatable = false)
    private Instant criadoEm;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Avaliacao> avaliacoes = new ArrayList<>();

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReacaoAvaliacao> reacoes = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Instant getCriadoEm() {
        return criadoEm;
    }

    public List<Avaliacao> getAvaliacoes() {
        return avaliacoes;
    }

    public List<ReacaoAvaliacao> getReacoes() {
        return reacoes;
    }
}
