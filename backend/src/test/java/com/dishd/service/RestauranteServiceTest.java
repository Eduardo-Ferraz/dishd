package com.dishd.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.dishd.domain.Categoria;
import com.dishd.domain.Restaurante;
import com.dishd.dto.RestauranteDTO;
import com.dishd.dto.RestauranteRequest;
import com.dishd.exception.ResourceNotFoundException;
import com.dishd.repository.CategoriaRepository;
import com.dishd.repository.RestauranteRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

/** Testes de CRUD e filtros de restaurantes ({@link RestauranteService}). */
@ExtendWith(MockitoExtension.class)
class RestauranteServiceTest {

    @Mock
    RestauranteRepository restauranteRepository;
    @Mock
    CategoriaRepository categoriaRepository;
    @InjectMocks
    RestauranteService service;

    private Restaurante restaurante(String nome) {
        Restaurante r = new Restaurante();
        r.setId(1L);
        r.setNome(nome);
        return r;
    }

    private Categoria categoria(Long id, String nome) {
        Categoria c = new Categoria(nome);
        c.setId(id);
        return c;
    }

    private final Pageable pageable = PageRequest.of(0, 20);

    @Test
    void listar_comBusca_usaFiltroPorNome() {
        when(restauranteRepository.findByNomeContainingIgnoreCaseOrderByNomeAsc(eq("pizza"), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(restaurante("Pizza da Praia"))));

        Page<RestauranteDTO> res = service.listar("pizza", null, pageable);

        assertThat(res.getContent()).hasSize(1);
        verify(restauranteRepository).findByNomeContainingIgnoreCaseOrderByNomeAsc(eq("pizza"), any(Pageable.class));
    }

    @Test
    void listar_comCategoria_usaFiltroPorCategoria() {
        when(restauranteRepository.findByCategorias_IdOrderByNomeAsc(eq(3L), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(restaurante("Sushi Praia"))));

        Page<RestauranteDTO> res = service.listar(null, 3L, pageable);

        assertThat(res.getContent()).hasSize(1);
        verify(restauranteRepository).findByCategorias_IdOrderByNomeAsc(eq(3L), any(Pageable.class));
    }

    @Test
    void listar_semFiltro_listaTudo() {
        when(restauranteRepository.findAllByOrderByNomeAsc(any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(restaurante("A"), restaurante("B"))));

        Page<RestauranteDTO> res = service.listar(null, null, pageable);

        assertThat(res.getContent()).hasSize(2);
        verify(restauranteRepository).findAllByOrderByNomeAsc(any(Pageable.class));
    }

    @Test
    void criar_resolveCategoriasESalva() {
        RestauranteRequest req = new RestauranteRequest("Novo", "Rua X", "foto", List.of(1L));
        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria(1L, "Italiana")));
        when(restauranteRepository.save(any(Restaurante.class))).thenAnswer(i -> i.getArgument(0));

        RestauranteDTO dto = service.criar(req);

        assertThat(dto.nome()).isEqualTo("Novo");
        assertThat(dto.categorias()).hasSize(1);
        assertThat(dto.categorias().get(0).nome()).isEqualTo("Italiana");
    }

    @Test
    void criar_categoriaInexistente_lancaNotFound() {
        RestauranteRequest req = new RestauranteRequest("Novo", "Rua X", "foto", List.of(99L));
        when(categoriaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.criar(req))
                .isInstanceOf(ResourceNotFoundException.class);
        verify(restauranteRepository, never()).save(any());
    }

    @Test
    void atualizar_alteraCampos() {
        Restaurante existente = restaurante("Antigo");
        when(restauranteRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(restauranteRepository.save(any(Restaurante.class))).thenAnswer(i -> i.getArgument(0));
        RestauranteRequest req = new RestauranteRequest("Atualizado", "Rua Y", "foto2", null);

        RestauranteDTO dto = service.atualizar(1L, req);

        assertThat(dto.nome()).isEqualTo("Atualizado");
        assertThat(dto.categorias()).isEmpty();
    }

    @Test
    void excluir_existente_remove() {
        Restaurante existente = restaurante("Alvo");
        when(restauranteRepository.findById(1L)).thenReturn(Optional.of(existente));

        service.excluir(1L);

        verify(restauranteRepository).delete(existente);
    }

    @Test
    void excluir_inexistente_lancaNotFound() {
        when(restauranteRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.excluir(1L))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}
