package com.dishd.config;

import com.dishd.domain.Avaliacao;
import com.dishd.domain.Categoria;
import com.dishd.domain.Reacao;
import com.dishd.domain.ReacaoAvaliacao;
import com.dishd.domain.Restaurante;
import com.dishd.domain.Usuario;
import com.dishd.repository.AvaliacaoRepository;
import com.dishd.repository.CategoriaRepository;
import com.dishd.repository.ReacaoAvaliacaoRepository;
import com.dishd.repository.RestauranteRepository;
import com.dishd.repository.UsuarioRepository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Popula o banco com dados de exemplo (categorias, restaurantes da Grande Vitoria,
 * usuarios e avaliacoes) na primeira inicializacao, para o frontend ja ter conteudo.
 *
 * <p>Usuario de teste: <b>demo@dishd.com</b> / senha <b>demo1234</b>.</p>
 */
@Component
public class DataSeeder implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final CategoriaRepository categoriaRepository;
    private final RestauranteRepository restauranteRepository;
    private final AvaliacaoRepository avaliacaoRepository;
    private final ReacaoAvaliacaoRepository reacaoRepository;
    private final PasswordEncoder passwordEncoder;

    public DataSeeder(UsuarioRepository usuarioRepository, CategoriaRepository categoriaRepository,
                      RestauranteRepository restauranteRepository, AvaliacaoRepository avaliacaoRepository,
                      ReacaoAvaliacaoRepository reacaoRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.categoriaRepository = categoriaRepository;
        this.restauranteRepository = restauranteRepository;
        this.avaliacaoRepository = avaliacaoRepository;
        this.reacaoRepository = reacaoRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) {
        if (usuarioRepository.count() > 0) {
            return; // ja populado
        }

        // ----- Categorias -----
        Categoria japonesa = novaCategoria("Japonesa");
        Categoria italiana = novaCategoria("Italiana");
        Categoria brasileira = novaCategoria("Brasileira");
        Categoria hamburgueria = novaCategoria("Hamburgueria");
        Categoria cafeteria = novaCategoria("Cafeteria");
        Categoria frutosDoMar = novaCategoria("Frutos do Mar");
        Categoria pizzaria = novaCategoria("Pizzaria");
        Categoria vegana = novaCategoria("Vegana");

        // ----- Restaurantes (Grande Vitoria/ES) -----
        Restaurante r1 = novoRestaurante("Cantina do Porto", "Av. Beira Mar, 100 - Vitoria",
                List.of(italiana, frutosDoMar));
        Restaurante r2 = novoRestaurante("Sushi Praia", "R. da Lama, 250 - Vitoria",
                List.of(japonesa));
        Restaurante r3 = novoRestaurante("Moqueca Capixaba", "Praca do Papa, 12 - Vitoria",
                List.of(brasileira, frutosDoMar));
        Restaurante r4 = novoRestaurante("Burger Camburi", "Av. Dante Michelini, 880 - Vitoria",
                List.of(hamburgueria));
        Restaurante r5 = novoRestaurante("Cafe da Pedra", "R. Sete de Setembro, 45 - Vila Velha",
                List.of(cafeteria, vegana));
        Restaurante r6 = novoRestaurante("Pizza da Praia", "Av. Hugo Musso, 300 - Vila Velha",
                List.of(pizzaria, italiana));

        // ----- Usuarios -----
        Usuario demo = novoUsuario("demo", "Usuario Demo", "demo@dishd.com", "27999990000", "demo1234");
        Usuario bianca = novoUsuario("bianca", "Bianca Souza", "bianca@dishd.com", "27999991111", "bianca123");

        // ----- Avaliacoes -----
        Avaliacao a1 = novaAvaliacao(demo, r3, 5.0,
                "Melhor moqueca da cidade, vista incrivel!", true);
        Avaliacao a2 = novaAvaliacao(demo, r2, 4.5,
                "Rodizio fresquinho e atendimento otimo.", false);
        Avaliacao a3 = novaAvaliacao(demo, r4, 4.0,
                "Hamburguer suculento, batata podia ser mais crocante.", false);
        Avaliacao a4 = novaAvaliacao(bianca, r1, 4.0,
                "Massa artesanal muito boa, ambiente aconchegante.", true);
        Avaliacao a5 = novaAvaliacao(bianca, r5, 5.0,
                "Opcoes veganas deliciosas e cafe especial.", true);
        Avaliacao a6 = novaAvaliacao(bianca, r3, 4.5,
                "Moqueca excelente, voltarei com certeza.", false);

        // ----- Reacoes -----
        novaReacao(bianca, a1, Reacao.LIKE);
        novaReacao(bianca, a3, Reacao.LIKE);
        novaReacao(demo, a4, Reacao.LIKE);
        novaReacao(demo, a5, Reacao.DISLIKE);

        // ----- Recalcula estatisticas dos restaurantes (entidades gerenciadas) -----
        List<Restaurante> restaurantes = restauranteRepository.findAll();
        for (Restaurante r : restaurantes) {
            double media = avaliacaoRepository.mediaNotaByRestaurante(r.getId());
            long total = avaliacaoRepository.countByRestaurante_Id(r.getId());
            r.setNotaMedia(Math.round(media * 10.0) / 10.0);
            r.setQntdAvaliacoes((int) total);
        }
        restauranteRepository.saveAll(restaurantes);
    }

    private Categoria novaCategoria(String nome) {
        return categoriaRepository.save(new Categoria(nome));
    }

    private Restaurante novoRestaurante(String nome, String endereco, List<Categoria> categorias) {
        Restaurante r = new Restaurante();
        r.setNome(nome);
        r.setEndereco(endereco);
        r.setFotoUrl("https://picsum.photos/seed/" + slug(nome) + "/600/400");
        r.setCategorias(new ArrayList<>(categorias));
        return restauranteRepository.save(r);
    }

    private Usuario novoUsuario(String username, String nome, String email, String telefone, String senha) {
        Usuario u = new Usuario();
        u.setUsername(username);
        u.setNome(nome);
        u.setEmail(email);
        u.setTelefone(telefone);
        u.setPassword(passwordEncoder.encode(senha));
        return usuarioRepository.save(u);
    }

    private Avaliacao novaAvaliacao(Usuario autor, Restaurante restaurante, double nota,
                                    String comentario, boolean favorito) {
        Avaliacao a = new Avaliacao();
        a.setUsuario(autor);
        a.setRestaurante(restaurante);
        a.setNota(nota);
        a.setComentario(comentario);
        a.setFavorito(favorito);
        a.setFotoUrl("https://picsum.photos/seed/" + slug(restaurante.getNome()) + "-prato/600/400");
        return avaliacaoRepository.save(a);
    }

    private ReacaoAvaliacao novaReacao(Usuario usuario, Avaliacao avaliacao, Reacao tipo) {
        ReacaoAvaliacao r = new ReacaoAvaliacao();
        r.setUsuario(usuario);
        r.setAvaliacao(avaliacao);
        r.setTipo(tipo);
        return reacaoRepository.save(r);
    }

    private String slug(String texto) {
        return texto.toLowerCase().replaceAll("[^a-z0-9]+", "-").replaceAll("(^-|-$)", "");
    }
}
