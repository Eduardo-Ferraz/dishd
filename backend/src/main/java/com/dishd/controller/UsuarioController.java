package com.dishd.controller;

import com.dishd.dto.AvaliacaoDTO;
import com.dishd.dto.EstatisticasDTO;
import com.dishd.dto.PagedResponse;
import com.dishd.dto.UsuarioDTO;
import com.dishd.service.AvaliacaoService;
import com.dishd.service.EstatisticaService;
import com.dishd.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/** Perfis de usuarios, seus diarios (avaliacoes) e estatisticas. */
@RestController
@RequestMapping("/api/usuarios")
@Tag(name = "Usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final AvaliacaoService avaliacaoService;
    private final EstatisticaService estatisticaService;

    public UsuarioController(UsuarioService usuarioService, AvaliacaoService avaliacaoService,
                             EstatisticaService estatisticaService) {
        this.usuarioService = usuarioService;
        this.avaliacaoService = avaliacaoService;
        this.estatisticaService = estatisticaService;
    }

    @GetMapping
    @Operation(summary = "Lista os usuarios")
    public List<UsuarioDTO> listar() {
        return usuarioService.listar();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca o perfil publico de um usuario")
    public UsuarioDTO buscar(@PathVariable Long id) {
        return usuarioService.buscar(id);
    }

    @GetMapping("/{id}/avaliacoes")
    @Operation(summary = "Diario do usuario: suas avaliacoes, mais recentes primeiro (paginado)")
    public PagedResponse<AvaliacaoDTO> avaliacoes(@PathVariable Long id,
                                                  @RequestParam(defaultValue = "0") int page,
                                                  @RequestParam(defaultValue = "20") int size) {
        return PagedResponse.from(avaliacaoService.porUsuario(id, PagedResponse.pageable(page, size)));
    }

    @GetMapping("/{id}/estatisticas")
    @Operation(summary = "Estatisticas de consumo do usuario")
    public EstatisticasDTO estatisticas(@PathVariable Long id) {
        return estatisticaService.doUsuario(id);
    }
}
