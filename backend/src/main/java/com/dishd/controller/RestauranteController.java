package com.dishd.controller;

import com.dishd.dto.AvaliacaoDTO;
import com.dishd.dto.RestauranteDTO;
import com.dishd.dto.RestauranteRequest;
import com.dishd.service.AvaliacaoService;
import com.dishd.service.RestauranteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/** Restaurantes e suas avaliacoes. */
@RestController
@RequestMapping("/api/restaurantes")
@Tag(name = "Restaurantes")
public class RestauranteController {

    private final RestauranteService restauranteService;
    private final AvaliacaoService avaliacaoService;

    public RestauranteController(RestauranteService restauranteService, AvaliacaoService avaliacaoService) {
        this.restauranteService = restauranteService;
        this.avaliacaoService = avaliacaoService;
    }

    @GetMapping
    @Operation(summary = "Lista restaurantes (filtros opcionais: busca por nome, categoriaId)")
    public List<RestauranteDTO> listar(@RequestParam(required = false) String busca,
                                       @RequestParam(required = false) Long categoriaId) {
        return restauranteService.listar(busca, categoriaId);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca um restaurante por id")
    public RestauranteDTO buscar(@PathVariable Long id) {
        return restauranteService.buscar(id);
    }

    @GetMapping("/{id}/avaliacoes")
    @Operation(summary = "Lista as avaliacoes de um restaurante")
    public List<AvaliacaoDTO> avaliacoes(@PathVariable Long id) {
        return avaliacaoService.porRestaurante(id);
    }

    @PostMapping
    @Operation(summary = "Cria um restaurante (requer token)")
    public ResponseEntity<RestauranteDTO> criar(@Valid @RequestBody RestauranteRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(restauranteService.criar(req));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza um restaurante (requer token)")
    public RestauranteDTO atualizar(@PathVariable Long id, @Valid @RequestBody RestauranteRequest req) {
        return restauranteService.atualizar(id, req);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remove um restaurante (requer token)")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        restauranteService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
