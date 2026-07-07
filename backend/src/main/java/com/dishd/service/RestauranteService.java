package com.dishd.service;

import com.dishd.domain.Categoria;
import com.dishd.domain.Restaurante;
import com.dishd.dto.RestauranteDTO;
import com.dishd.dto.RestauranteRequest;
import com.dishd.exception.ResourceNotFoundException;
import com.dishd.repository.CategoriaRepository;
import com.dishd.repository.RestauranteRepository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** CRUD de restaurantes e suas categorias. */
@Service
public class RestauranteService {

    private final RestauranteRepository restauranteRepository;
    private final CategoriaRepository categoriaRepository;

    public RestauranteService(RestauranteRepository restauranteRepository,
                              CategoriaRepository categoriaRepository) {
        this.restauranteRepository = restauranteRepository;
        this.categoriaRepository = categoriaRepository;
    }

    @Transactional(readOnly = true)
    public Page<RestauranteDTO> listar(String busca, Long categoriaId, Pageable pageable) {
        Page<Restaurante> restaurantes;
        if (busca != null && !busca.isBlank()) {
            restaurantes = restauranteRepository.findByNomeContainingIgnoreCaseOrderByNomeAsc(busca.trim(), pageable);
        } else if (categoriaId != null) {
            restaurantes = restauranteRepository.findByCategorias_IdOrderByNomeAsc(categoriaId, pageable);
        } else {
            restaurantes = restauranteRepository.findAllByOrderByNomeAsc(pageable);
        }
        return restaurantes.map(RestauranteDTO::from);
    }

    @Transactional(readOnly = true)
    public RestauranteDTO buscar(Long id) {
        return RestauranteDTO.from(buscarEntidade(id));
    }

    @Transactional
    public RestauranteDTO criar(RestauranteRequest req) {
        Restaurante r = new Restaurante();
        aplicar(r, req);
        return RestauranteDTO.from(restauranteRepository.save(r));
    }

    @Transactional
    public RestauranteDTO atualizar(Long id, RestauranteRequest req) {
        Restaurante r = buscarEntidade(id);
        aplicar(r, req);
        return RestauranteDTO.from(restauranteRepository.save(r));
    }

    @Transactional
    public void excluir(Long id) {
        Restaurante r = buscarEntidade(id);
        restauranteRepository.delete(r);
    }

    private Restaurante buscarEntidade(Long id) {
        return restauranteRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.of("Restaurante", id));
    }

    private void aplicar(Restaurante r, RestauranteRequest req) {
        r.setNome(req.nome());
        r.setEndereco(req.endereco());
        r.setFotoUrl(req.fotoUrl());
        r.setCategorias(resolverCategorias(req.categoriaIds()));
    }

    private List<Categoria> resolverCategorias(List<Long> ids) {
        List<Categoria> categorias = new ArrayList<>();
        if (ids == null) {
            return categorias;
        }
        for (Long id : ids) {
            categorias.add(categoriaRepository.findById(id)
                    .orElseThrow(() -> ResourceNotFoundException.of("Categoria", id)));
        }
        return categorias;
    }
}
