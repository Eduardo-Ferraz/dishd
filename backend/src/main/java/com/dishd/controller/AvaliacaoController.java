package com.dishd.controller;

import com.dishd.dto.AvaliacaoDTO;
import com.dishd.dto.AvaliacaoRequest;
import com.dishd.dto.PagedResponse;
import com.dishd.dto.ReacaoRequest;
import com.dishd.service.AvaliacaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
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

/** Avaliacoes, feed social e reacoes (like/dislike). */
@RestController
@RequestMapping("/api/avaliacoes")
@Tag(name = "Avaliacoes")
public class AvaliacaoController {

    private static final int MAX_SIZE = 100;

    private final AvaliacaoService avaliacaoService;

    public AvaliacaoController(AvaliacaoService avaliacaoService) {
        this.avaliacaoService = avaliacaoService;
    }

    @GetMapping
    @Operation(summary = "Feed social: avaliacoes mais recentes, paginadas")
    public PagedResponse<AvaliacaoDTO> feed(@RequestParam(defaultValue = "0") int page,
                                            @RequestParam(defaultValue = "20") int size) {
        int safeSize = Math.min(Math.max(size, 1), MAX_SIZE);
        int safePage = Math.max(page, 0);
        return PagedResponse.from(avaliacaoService.feed(PageRequest.of(safePage, safeSize)));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca uma avaliacao por id")
    public AvaliacaoDTO buscar(@PathVariable Long id) {
        return avaliacaoService.buscar(id);
    }

    @PostMapping
    @Operation(summary = "Cria uma avaliacao (requer token)")
    public ResponseEntity<AvaliacaoDTO> criar(@Valid @RequestBody AvaliacaoRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(avaliacaoService.criar(req));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza uma avaliacao propria (requer token)")
    public AvaliacaoDTO atualizar(@PathVariable Long id, @Valid @RequestBody AvaliacaoRequest req) {
        return avaliacaoService.atualizar(id, req);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remove uma avaliacao propria (requer token)")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        avaliacaoService.excluir(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/reacoes")
    @Operation(summary = "Curte ou descurte uma avaliacao (requer token)")
    public AvaliacaoDTO reagir(@PathVariable Long id, @Valid @RequestBody ReacaoRequest req) {
        return avaliacaoService.reagir(id, req);
    }

    @DeleteMapping("/{id}/reacoes")
    @Operation(summary = "Remove a sua reacao de uma avaliacao (requer token)")
    public AvaliacaoDTO removerReacao(@PathVariable Long id) {
        return avaliacaoService.removerReacao(id);
    }
}
