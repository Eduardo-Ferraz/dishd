package com.dishd.service;

import com.dishd.domain.Categoria;
import com.dishd.dto.CategoriaDTO;
import com.dishd.dto.CategoriaRequest;
import com.dishd.exception.BadRequestException;
import com.dishd.exception.ResourceNotFoundException;
import com.dishd.repository.CategoriaRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** CRUD de categorias. */
@Service
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    public CategoriaService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    @Transactional(readOnly = true)
    public List<CategoriaDTO> listar() {
        return categoriaRepository.findAll().stream().map(CategoriaDTO::from).toList();
    }

    @Transactional(readOnly = true)
    public CategoriaDTO buscar(Long id) {
        return categoriaRepository.findById(id)
                .map(CategoriaDTO::from)
                .orElseThrow(() -> ResourceNotFoundException.of("Categoria", id));
    }

    @Transactional
    public CategoriaDTO criar(CategoriaRequest req) {
        if (categoriaRepository.existsByNomeIgnoreCase(req.nome())) {
            throw new BadRequestException("Categoria ja existe: " + req.nome());
        }
        Categoria salva = categoriaRepository.save(new Categoria(req.nome()));
        return CategoriaDTO.from(salva);
    }
}
