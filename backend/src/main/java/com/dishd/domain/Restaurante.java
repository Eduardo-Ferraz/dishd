package com.dishd.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;

/**
 * Restaurante avaliado pelos usuarios.
 *
 * <p>Os campos {@code notaMedia} e {@code qntdAvaliacoes} sao desnormalizados:
 * o sistema os recalcula sempre que uma avaliacao do restaurante e criada,
 * editada ou removida.</p>
 */
@Entity
@Table(name = "restaurante")
public class Restaurante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    private String endereco;

    @Column(name = "nota_media")
    private Double notaMedia = 0.0;

    @Column(name = "qntd_avaliacoes")
    private Integer qntdAvaliacoes = 0;

    @Column(name = "foto_url", length = 1000)
    private String fotoUrl;

    /** Relacao muitos-para-muitos com Categoria (tabela de juncao categoria_restaurante). */
    @ManyToMany
    @JoinTable(name = "categoria_restaurante",
            joinColumns = @JoinColumn(name = "restaurante_id"),
            inverseJoinColumns = @JoinColumn(name = "categoria_id"))
    private List<Categoria> categorias = new ArrayList<>();

    @OneToMany(mappedBy = "restaurante", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Avaliacao> avaliacoes = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public Double getNotaMedia() {
        return notaMedia;
    }

    public void setNotaMedia(Double notaMedia) {
        this.notaMedia = notaMedia;
    }

    public Integer getQntdAvaliacoes() {
        return qntdAvaliacoes;
    }

    public void setQntdAvaliacoes(Integer qntdAvaliacoes) {
        this.qntdAvaliacoes = qntdAvaliacoes;
    }

    public String getFotoUrl() {
        return fotoUrl;
    }

    public void setFotoUrl(String fotoUrl) {
        this.fotoUrl = fotoUrl;
    }

    public List<Categoria> getCategorias() {
        return categorias;
    }

    public void setCategorias(List<Categoria> categorias) {
        this.categorias = categorias;
    }

    public List<Avaliacao> getAvaliacoes() {
        return avaliacoes;
    }
}
