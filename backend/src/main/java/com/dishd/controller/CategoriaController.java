package com.dishd.controller;

import com.dishd.dto.CategoriaDTO;
import com.dishd.dto.CategoriaRequest;
import com.dishd.service.CategoriaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Categorias de restaurantes. */
@RestController
@RequestMapping("/api/categorias")
@Tag(name = "Categorias")
public class CategoriaController {

    private final CategoriaService categoriaService;

    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    @GetMapping
    @Operation(summary = "Lista todas as categorias")
    public List<CategoriaDTO> listar() {
        return categoriaService.listar();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca uma categoria por id")
    public CategoriaDTO buscar(@PathVariable Long id) {
        return categoriaService.buscar(id);
    }

    @PostMapping
    @Operation(summary = "Cria uma categoria (requer token)")
    public ResponseEntity<CategoriaDTO> criar(@Valid @RequestBody CategoriaRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(categoriaService.criar(req));
    }
}
