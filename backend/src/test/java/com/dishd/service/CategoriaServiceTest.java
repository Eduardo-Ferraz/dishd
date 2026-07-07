package com.dishd.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.dishd.domain.Categoria;
import com.dishd.dto.CategoriaDTO;
import com.dishd.dto.CategoriaRequest;
import com.dishd.exception.BadRequestException;
import com.dishd.exception.ResourceNotFoundException;
import com.dishd.repository.CategoriaRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/** Testes de CRUD de categorias ({@link CategoriaService}). */
@ExtendWith(MockitoExtension.class)
class CategoriaServiceTest {

    @Mock
    CategoriaRepository categoriaRepository;
    @InjectMocks
    CategoriaService service;

    @Test
    void listar_retornaTodas() {
        when(categoriaRepository.findAll()).thenReturn(List.of(new Categoria("Japonesa"), new Categoria("Italiana")));

        List<CategoriaDTO> res = service.listar();

        assertThat(res).hasSize(2);
    }

    @Test
    void buscar_existente_retornaDTO() {
        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(new Categoria("Vegana")));

        CategoriaDTO dto = service.buscar(1L);

        assertThat(dto.nome()).isEqualTo("Vegana");
    }

    @Test
    void buscar_inexistente_lancaNotFound() {
        when(categoriaRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.buscar(1L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void criar_nova_salva() {
        CategoriaRequest req = new CategoriaRequest("Cafeteria");
        when(categoriaRepository.existsByNomeIgnoreCase("Cafeteria")).thenReturn(false);
        when(categoriaRepository.save(any(Categoria.class))).thenAnswer(i -> i.getArgument(0));

        CategoriaDTO dto = service.criar(req);

        assertThat(dto.nome()).isEqualTo("Cafeteria");
    }

    @Test
    void criar_duplicada_lancaBadRequest() {
        CategoriaRequest req = new CategoriaRequest("Japonesa");
        when(categoriaRepository.existsByNomeIgnoreCase("Japonesa")).thenReturn(true);

        assertThatThrownBy(() -> service.criar(req))
                .isInstanceOf(BadRequestException.class);
        verify(categoriaRepository, never()).save(any());
    }
}
