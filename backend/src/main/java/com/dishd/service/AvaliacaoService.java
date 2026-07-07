package com.dishd.service;

import com.dishd.domain.Avaliacao;
import com.dishd.domain.Reacao;
import com.dishd.domain.ReacaoAvaliacao;
import com.dishd.domain.Restaurante;
import com.dishd.domain.Usuario;
import com.dishd.dto.AvaliacaoDTO;
import com.dishd.dto.AvaliacaoRequest;
import com.dishd.dto.ReacaoRequest;
import com.dishd.exception.ForbiddenException;
import com.dishd.exception.ResourceNotFoundException;
import com.dishd.repository.AvaliacaoRepository;
import com.dishd.repository.ReacaoAvaliacaoRepository;
import com.dishd.repository.ReacaoContagem;
import com.dishd.repository.RestauranteRepository;
import com.dishd.security.CurrentUserService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** Regras de avaliacoes, feed social e reacoes. */
@Service
public class AvaliacaoService {

    private final AvaliacaoRepository avaliacaoRepository;
    private final RestauranteRepository restauranteRepository;
    private final ReacaoAvaliacaoRepository reacaoRepository;
    private final CurrentUserService currentUserService;

    public AvaliacaoService(AvaliacaoRepository avaliacaoRepository,
                            RestauranteRepository restauranteRepository,
                            ReacaoAvaliacaoRepository reacaoRepository,
                            CurrentUserService currentUserService) {
        this.avaliacaoRepository = avaliacaoRepository;
        this.restauranteRepository = restauranteRepository;
        this.reacaoRepository = reacaoRepository;
        this.currentUserService = currentUserService;
    }

    // ---------------- Consultas ----------------

    /** Feed social: avaliacoes mais recentes, paginadas. */
    @Transactional(readOnly = true)
    public Page<AvaliacaoDTO> feed(Pageable pageable) {
        Long currentUserId = currentUserService.getCurrentUsuarioIdOrNull();
        return paginarComDTO(avaliacaoRepository.findAllByOrderByCriadoEmDesc(pageable), currentUserId);
    }

    @Transactional(readOnly = true)
    public AvaliacaoDTO buscar(Long id) {
        Long currentUserId = currentUserService.getCurrentUsuarioIdOrNull();
        return toDTO(buscarEntidade(id), currentUserId);
    }

    @Transactional(readOnly = true)
    public Page<AvaliacaoDTO> porUsuario(Long usuarioId, Pageable pageable) {
        Long currentUserId = currentUserService.getCurrentUsuarioIdOrNull();
        return paginarComDTO(
                avaliacaoRepository.findByUsuario_IdOrderByCriadoEmDesc(usuarioId, pageable), currentUserId);
    }

    @Transactional(readOnly = true)
    public Page<AvaliacaoDTO> porRestaurante(Long restauranteId, Pageable pageable) {
        Long currentUserId = currentUserService.getCurrentUsuarioIdOrNull();
        return paginarComDTO(
                avaliacaoRepository.findByRestaurante_IdOrderByCriadoEmDesc(restauranteId, pageable), currentUserId);
    }

    /** Converte uma pagina de entidades em DTOs usando contagens de reacao em lote (sem N+1). */
    private Page<AvaliacaoDTO> paginarComDTO(Page<Avaliacao> page, Long currentUserId) {
        List<AvaliacaoDTO> dtos = toDTOs(page.getContent(), currentUserId);
        return new PageImpl<>(dtos, page.getPageable(), page.getTotalElements());
    }

    // ---------------- Escrita ----------------

    @Transactional
    public AvaliacaoDTO criar(AvaliacaoRequest req) {
        Usuario autor = currentUserService.getCurrentUsuario();
        Restaurante restaurante = restauranteRepository.findById(req.restauranteId())
                .orElseThrow(() -> ResourceNotFoundException.of("Restaurante", req.restauranteId()));

        Avaliacao a = new Avaliacao();
        a.setUsuario(autor);
        a.setRestaurante(restaurante);
        aplicarCampos(a, req);
        avaliacaoRepository.save(a);

        recalcularEstatisticas(restaurante);
        return toDTO(a, autor.getId());
    }

    @Transactional
    public AvaliacaoDTO atualizar(Long id, AvaliacaoRequest req) {
        Usuario autor = currentUserService.getCurrentUsuario();
        Avaliacao a = buscarEntidade(id);
        garantirAutor(a, autor);

        Restaurante anterior = a.getRestaurante();
        if (!anterior.getId().equals(req.restauranteId())) {
            Restaurante novo = restauranteRepository.findById(req.restauranteId())
                    .orElseThrow(() -> ResourceNotFoundException.of("Restaurante", req.restauranteId()));
            a.setRestaurante(novo);
        }
        aplicarCampos(a, req);
        avaliacaoRepository.save(a);

        recalcularEstatisticas(anterior);
        if (!anterior.getId().equals(a.getRestaurante().getId())) {
            recalcularEstatisticas(a.getRestaurante());
        }
        return toDTO(a, autor.getId());
    }

    @Transactional
    public void excluir(Long id) {
        Usuario autor = currentUserService.getCurrentUsuario();
        Avaliacao a = buscarEntidade(id);
        garantirAutor(a, autor);
        Restaurante restaurante = a.getRestaurante();
        avaliacaoRepository.delete(a);
        recalcularEstatisticas(restaurante);
    }

    // ---------------- Reacoes ----------------

    @Transactional
    public AvaliacaoDTO reagir(Long avaliacaoId, ReacaoRequest req) {
        Usuario usuario = currentUserService.getCurrentUsuario();
        Avaliacao a = buscarEntidade(avaliacaoId);
        ReacaoAvaliacao reacao = reacaoRepository
                .findByUsuario_IdAndAvaliacao_Id(usuario.getId(), avaliacaoId)
                .orElseGet(() -> {
                    ReacaoAvaliacao nova = new ReacaoAvaliacao();
                    nova.setUsuario(usuario);
                    nova.setAvaliacao(a);
                    return nova;
                });
        reacao.setTipo(req.tipo());
        reacaoRepository.save(reacao);
        return toDTO(a, usuario.getId());
    }

    @Transactional
    public AvaliacaoDTO removerReacao(Long avaliacaoId) {
        Usuario usuario = currentUserService.getCurrentUsuario();
        Avaliacao a = buscarEntidade(avaliacaoId);
        reacaoRepository.findByUsuario_IdAndAvaliacao_Id(usuario.getId(), avaliacaoId)
                .ifPresent(reacaoRepository::delete);
        return toDTO(a, usuario.getId());
    }

    // ---------------- Auxiliares ----------------

    private Avaliacao buscarEntidade(Long id) {
        return avaliacaoRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.of("Avaliacao", id));
    }

    private void garantirAutor(Avaliacao a, Usuario usuario) {
        if (!a.getUsuario().getId().equals(usuario.getId())) {
            throw new ForbiddenException("Voce so pode alterar suas proprias avaliacoes");
        }
    }

    private void aplicarCampos(Avaliacao a, AvaliacaoRequest req) {
        a.setNota(req.nota());
        a.setComentario(req.comentario());
        a.setFotoUrl(req.fotoUrl());
        a.setFavorito(req.favorito());
    }

    /** Recalcula nota media e quantidade de avaliacoes do restaurante. */
    private void recalcularEstatisticas(Restaurante restaurante) {
        double media = avaliacaoRepository.mediaNotaByRestaurante(restaurante.getId());
        long total = avaliacaoRepository.countByRestaurante_Id(restaurante.getId());
        restaurante.setNotaMedia(Math.round(media * 10.0) / 10.0);
        restaurante.setQntdAvaliacoes((int) total);
        restauranteRepository.save(restaurante);
    }

    /** DTO de uma unica avaliacao (contagens consultadas individualmente). */
    private AvaliacaoDTO toDTO(Avaliacao a, Long currentUserId) {
        long likes = reacaoRepository.countByAvaliacao_IdAndTipo(a.getId(), Reacao.LIKE);
        long dislikes = reacaoRepository.countByAvaliacao_IdAndTipo(a.getId(), Reacao.DISLIKE);
        Reacao minhaReacao = null;
        if (currentUserId != null) {
            minhaReacao = reacaoRepository.findByUsuario_IdAndAvaliacao_Id(currentUserId, a.getId())
                    .map(ReacaoAvaliacao::getTipo).orElse(null);
        }
        return montarDTO(a, likes, dislikes, minhaReacao);
    }

    /** DTOs de varias avaliacoes com contagens/reacoes carregadas em lote (evita N+1). */
    private List<AvaliacaoDTO> toDTOs(List<Avaliacao> avaliacoes, Long currentUserId) {
        if (avaliacoes.isEmpty()) {
            return List.of();
        }
        List<Long> ids = avaliacoes.stream().map(Avaliacao::getId).toList();

        Map<Long, Long> likes = new HashMap<>();
        Map<Long, Long> dislikes = new HashMap<>();
        for (ReacaoContagem c : reacaoRepository.contarReacoesPorAvaliacaoIds(ids)) {
            if (c.getTipo() == Reacao.LIKE) {
                likes.put(c.getAvaliacaoId(), c.getTotal());
            } else if (c.getTipo() == Reacao.DISLIKE) {
                dislikes.put(c.getAvaliacaoId(), c.getTotal());
            }
        }

        Map<Long, Reacao> minhasReacoes = new HashMap<>();
        if (currentUserId != null) {
            for (ReacaoAvaliacao r : reacaoRepository.findByUsuario_IdAndAvaliacao_IdIn(currentUserId, ids)) {
                minhasReacoes.put(r.getAvaliacao().getId(), r.getTipo());
            }
        }

        return avaliacoes.stream()
                .map(a -> montarDTO(a,
                        likes.getOrDefault(a.getId(), 0L),
                        dislikes.getOrDefault(a.getId(), 0L),
                        minhasReacoes.get(a.getId())))
                .toList();
    }

    private AvaliacaoDTO montarDTO(Avaliacao a, long likes, long dislikes, Reacao minhaReacao) {
        Usuario u = a.getUsuario();
        Restaurante r = a.getRestaurante();
        return new AvaliacaoDTO(
                a.getId(), a.getNota(), a.getComentario(), a.getFotoUrl(), a.isFavorito(), a.getCriadoEm(),
                u.getId(), u.getUsername(), u.getNome(),
                r.getId(), r.getNome(), r.getFotoUrl(),
                likes, dislikes, minhaReacao);
    }
}
