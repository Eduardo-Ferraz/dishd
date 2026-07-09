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

        // ----- Restaurantes reais da Grande Vitoria/ES -----
        Restaurante r1 = novoRestaurante("Soeta", "R. Aluisio Simoes, 887 - Bento Ferreira, Vitoria",
                List.of(brasileira));
        Restaurante r2 = novoRestaurante("Lareira Portuguesa", "R. Joao da Cruz, 111 - Praia do Canto, Vitoria",
                List.of(brasileira, frutosDoMar));
        Restaurante r3 = novoRestaurante("Papaguth", "Av. Nossa Sra. dos Navegantes, 700 - Enseada do Sua, Vitoria",
                List.of(frutosDoMar, brasileira));
        Restaurante r4 = novoRestaurante("Restaurante Atlantica", "Av. Saturnino de Brito, 1187 - Praia do Canto, Vitoria",
                List.of(frutosDoMar));
        Restaurante r5 = novoRestaurante("Partido Alto", "Av. Dante Michelini, 1030 - Jardim da Penha, Vitoria",
                List.of(frutosDoMar, brasileira));
        Restaurante r6 = novoRestaurante("Bistro Saldanha", "Av. Marechal Mascarenhas de Moraes, 855 - Centro, Vitoria",
                List.of(brasileira));
        Restaurante r7 = novoRestaurante("Senac Ilha do Boi", "R. Aleixo Netto, 577 - Praia do Canto, Vitoria",
                List.of(brasileira, frutosDoMar));
        Restaurante r8 = novoRestaurante("Kensei Sushi", "Av. Cel. Jose Martins de Figueiredo, 349 - Tabuazeiro, Vitoria",
                List.of(japonesa));
        Restaurante r9 = novoRestaurante("Suzushi", "R. Aleixo Netto, 224 - Praia do Canto, Vitoria",
                List.of(japonesa));
        Restaurante r10 = novoRestaurante("Melt Hamburgueria", "R. Affonso Claudio, 259 - Praia do Canto, Vitoria",
                List.of(hamburgueria));
        Restaurante r11 = novoRestaurante("Rick's Burguer", "Av. Ministro Salgado Filho, 1672 - Soteco, Vila Velha",
                List.of(hamburgueria));
        Restaurante r12 = novoRestaurante("Casa Aure", "R. Chapot Presvot, 401 - Praia do Canto, Vitoria",
                List.of(vegana, cafeteria));
        Restaurante r13 = novoRestaurante("Casa Graviola", "R. Elesbao Linhares, 41 - Praia do Canto, Vitoria",
                List.of(vegana));
        Restaurante r14 = novoRestaurante("Armazem Natural", "Av. Judith Leao Castelo, 82 - Jardim Camburi, Vitoria",
                List.of(vegana, cafeteria));
        Restaurante r15 = novoRestaurante("Kranmilly", "Av. Judith Leao Castelo, 360 - Jardim Camburi, Vitoria",
                List.of(pizzaria));
        Restaurante r16 = novoRestaurante("Monte Libano", "R. Aleixo Netto, 130 - Praia do Canto, Vitoria",
                List.of(italiana, cafeteria));
        Restaurante r17 = novoRestaurante("Mahai", "R. Constante Sodre, 780 - Praia do Canto, Vitoria",
                List.of(cafeteria, vegana));
        Restaurante r18 = novoRestaurante("Camarada Camarao", "Av. Americo Buaiz, 200 - Enseada do Sua, Vitoria",
                List.of(frutosDoMar));

        // ----- Usuarios -----
        Usuario demo = novoUsuario("demo", "Usuario Demo", "demo@dishd.com", "27999990000", "demo1234");
        Usuario bianca = novoUsuario("bianca", "Bianca Souza", "bianca@dishd.com", "27999991111", "bianca123");
        Usuario carlos = novoUsuario("carlos", "Carlos Mendes", "carlos@dishd.com", "27999992222", "carlos123");
        Usuario ana = novoUsuario("ana", "Ana Ferreira", "ana@dishd.com", "27999993333", "ana12345");
        Usuario rafael = novoUsuario("rafael", "Rafael Lima", "rafael@dishd.com", "27999994444", "rafael123");
        Usuario juliana = novoUsuario("juliana", "Juliana Costa", "juliana@dishd.com", "27999995555", "juliana12");
        Usuario pedro = novoUsuario("pedro", "Pedro Alves", "pedro@dishd.com", "27999996666", "pedro123");
        Usuario mariana = novoUsuario("mariana", "Mariana Rocha", "mariana@dishd.com", "27999997777", "mariana12");

        // ----- Avaliacoes -----
        Avaliacao a1 = novaAvaliacao(demo, r3, 5.0,
                "Moqueca capixaba impecavel e vista pra Praca do Papa. Melhor da cidade!", true);
        Avaliacao a2 = novaAvaliacao(demo, r9, 4.5,
                "Sushi fresquinho na Praia do Canto, atendimento otimo.", false);
        Avaliacao a3 = novaAvaliacao(demo, r10, 4.0,
                "Hamburguer suculento, mas fila grande no fim de semana.", false);
        Avaliacao a4 = novaAvaliacao(bianca, r1, 5.0,
                "Menu degustacao do Soeta e uma experiencia, alta gastronomia de verdade.", true);
        Avaliacao a5 = novaAvaliacao(bianca, r12, 5.0,
                "Cafe plant-based delicioso, ambiente lindo na Chapot Presvot.", true);
        Avaliacao a6 = novaAvaliacao(bianca, r3, 4.5,
                "Frutos do mar frescos, voltarei com certeza.", false);
        Avaliacao a7 = novaAvaliacao(carlos, r8, 4.0,
                "Kensei mantem o padrao, peixe de qualidade em Tabuazeiro.", false);
        Avaliacao a8 = novaAvaliacao(carlos, r11, 5.0,
                "Rick's e classico, melhor hamburguer da Grande Vitoria.", true);
        Avaliacao a9 = novaAvaliacao(carlos, r16, 3.5,
                "Padaria tradicional, mas o salgado estava frio.", false);
        Avaliacao a10 = novaAvaliacao(ana, r13, 5.0,
                "Casa Graviola tem opcoes veganas criativas, adorei.", true);
        Avaliacao a11 = novaAvaliacao(ana, r14, 4.0,
                "Almoco natural farto no Armazem, bom custo-beneficio.", false);
        Avaliacao a12 = novaAvaliacao(ana, r15, 4.5,
                "Pizza artesanal da Kranmilly com massa leve, recomendo.", true);
        Avaliacao a13 = novaAvaliacao(rafael, r10, 4.5,
                "Melt caprichou no smash, batata rustica otima.", false);
        Avaliacao a14 = novaAvaliacao(rafael, r5, 5.0,
                "Partido Alto serve a melhor moqueca de Jardim da Penha.", true);
        Avaliacao a15 = novaAvaliacao(rafael, r4, 4.0,
                "Atlantica tradicional, peixe fresco e porcao farta.", false);
        Avaliacao a16 = novaAvaliacao(juliana, r7, 4.0,
                "Senac Ilha do Boi tem vista de tirar o folego.", true);
        Avaliacao a17 = novaAvaliacao(juliana, r9, 4.5,
                "Suzushi bem servido, chef atencioso.", false);
        Avaliacao a18 = novaAvaliacao(juliana, r17, 3.5,
                "Mahai bacana, mas lotado demais na hora do almoco.", false);
        Avaliacao a19 = novaAvaliacao(pedro, r6, 5.0,
                "Bistro Saldanha surpreende no Centro, pratos bem elaborados.", true);
        Avaliacao a20 = novaAvaliacao(pedro, r18, 4.0,
                "Camarada Camarao no Shopping Vitoria, camarao farto.", false);
        Avaliacao a21 = novaAvaliacao(pedro, r2, 4.5,
                "Lareira Portuguesa e referencia em moqueca ha decadas.", true);
        Avaliacao a22 = novaAvaliacao(mariana, r12, 5.0,
                "Melhor cafe vegano da Praia do Canto, disparado.", true);
        Avaliacao a23 = novaAvaliacao(mariana, r8, 4.0,
                "Kensei com sushi fresco e ambiente aconchegante.", false);
        Avaliacao a24 = novaAvaliacao(mariana, r15, 4.0,
                "Kranmilly faz massa e pao artesanal, atendimento nota dez.", false);
        Avaliacao a25 = novaAvaliacao(demo, r1, 4.5,
                "Soeta vale cada centavo, degustacao memoravel.", false);
        Avaliacao a26 = novaAvaliacao(demo, r5, 5.0,
                "Moqueca do Partido Alto e sensacional, peixe fresquinho.", true);
        Avaliacao a27 = novaAvaliacao(demo, r15, 4.0,
                "Pizza da Kranmilly com massa leve e bem saborosa.", false);
        Avaliacao a28 = novaAvaliacao(bianca, r10, 4.0,
                "Melt lotado mas o burger compensa a espera.", false);
        Avaliacao a29 = novaAvaliacao(bianca, r8, 4.5,
                "Kensei nunca decepciona no sushi, peixe de primeira.", true);
        Avaliacao a30 = novaAvaliacao(bianca, r18, 4.0,
                "Camarada Camarao no dende otimo no Shopping Vitoria.", false);
        Avaliacao a31 = novaAvaliacao(carlos, r2, 5.0,
                "Lareira Portuguesa, moqueca tradicional imbativel.", true);
        Avaliacao a32 = novaAvaliacao(carlos, r5, 4.5,
                "Partido Alto com vista pro mar e peixe fresco.", false);
        Avaliacao a33 = novaAvaliacao(carlos, r12, 4.0,
                "Casa Aure, cafe da tarde vegano surpreende.", false);
        Avaliacao a34 = novaAvaliacao(ana, r17, 4.5,
                "Mahai tem poke bowls fresquinhos e coloridos.", true);
        Avaliacao a35 = novaAvaliacao(ana, r1, 5.0,
                "Soeta e a melhor experiencia gastronomica do ES.", true);
        Avaliacao a36 = novaAvaliacao(ana, r3, 4.5,
                "Papaguth, frutos do mar de primeira e vista linda.", false);
        Avaliacao a37 = novaAvaliacao(rafael, r11, 5.0,
                "Rick's classico, hamburguer que nao sai de moda.", true);
        Avaliacao a38 = novaAvaliacao(rafael, r8, 4.0,
                "Kensei tem bom sushi mas a espera e longa.", false);
        Avaliacao a39 = novaAvaliacao(rafael, r15, 4.5,
                "Kranmilly, pizza artesanal excelente em Jardim Camburi.", false);
        Avaliacao a40 = novaAvaliacao(juliana, r10, 4.0,
                "Melt com burger suculento e ambiente descolado.", false);
        Avaliacao a41 = novaAvaliacao(juliana, r13, 4.5,
                "Casa Graviola tem opcoes veganas bem criativas.", true);
        Avaliacao a42 = novaAvaliacao(juliana, r3, 5.0,
                "Papaguth, moqueca impecavel e chef nota dez.", true);
        Avaliacao a43 = novaAvaliacao(pedro, r1, 5.0,
                "Soeta, menu degustacao inesquecivel.", true);
        Avaliacao a44 = novaAvaliacao(pedro, r10, 4.0,
                "Melt tem bom burger mas fila grande no fim de semana.", false);
        Avaliacao a45 = novaAvaliacao(pedro, r8, 4.5,
                "Kensei, sushi fresco em Tabuazeiro, recomendo.", false);
        Avaliacao a46 = novaAvaliacao(mariana, r5, 5.0,
                "Partido Alto, melhor moqueca da regiao disparado.", true);
        Avaliacao a47 = novaAvaliacao(mariana, r11, 4.0,
                "Rick's tradicional, batata sempre otima.", false);
        Avaliacao a48 = novaAvaliacao(mariana, r17, 4.5,
                "Mahai, ambiente lindo e comida leve.", true);
        Avaliacao a49 = novaAvaliacao(demo, r7, 4.0,
                "Senac Ilha do Boi, vista incrivel no jantar.", false);
        Avaliacao a50 = novaAvaliacao(bianca, r6, 4.5,
                "Bistro Saldanha, pratos elaborados no Centro.", true);

        // ----- Reacoes -----
        novaReacao(bianca, a1, Reacao.LIKE);
        novaReacao(bianca, a3, Reacao.LIKE);
        novaReacao(demo, a4, Reacao.LIKE);
        novaReacao(demo, a5, Reacao.DISLIKE);
        novaReacao(carlos, a1, Reacao.LIKE);
        novaReacao(ana, a1, Reacao.LIKE);
        novaReacao(rafael, a8, Reacao.LIKE);
        novaReacao(juliana, a8, Reacao.LIKE);
        novaReacao(pedro, a10, Reacao.LIKE);
        novaReacao(mariana, a10, Reacao.LIKE);
        novaReacao(demo, a14, Reacao.LIKE);
        novaReacao(bianca, a19, Reacao.LIKE);
        novaReacao(carlos, a22, Reacao.LIKE);
        novaReacao(ana, a12, Reacao.LIKE);
        novaReacao(rafael, a16, Reacao.DISLIKE);
        novaReacao(juliana, a2, Reacao.LIKE);
        novaReacao(pedro, a21, Reacao.LIKE);
        novaReacao(mariana, a9, Reacao.DISLIKE);
        novaReacao(demo, a31, Reacao.LIKE);
        novaReacao(demo, a35, Reacao.LIKE);
        novaReacao(bianca, a26, Reacao.LIKE);
        novaReacao(bianca, a37, Reacao.LIKE);
        novaReacao(carlos, a25, Reacao.LIKE);
        novaReacao(carlos, a46, Reacao.LIKE);
        novaReacao(ana, a43, Reacao.LIKE);
        novaReacao(ana, a29, Reacao.LIKE);
        novaReacao(rafael, a42, Reacao.LIKE);
        novaReacao(rafael, a31, Reacao.LIKE);
        novaReacao(juliana, a35, Reacao.LIKE);
        novaReacao(juliana, a26, Reacao.LIKE);
        novaReacao(pedro, a46, Reacao.LIKE);
        novaReacao(pedro, a41, Reacao.LIKE);
        novaReacao(mariana, a43, Reacao.LIKE);
        novaReacao(mariana, a37, Reacao.LIKE);
        novaReacao(demo, a41, Reacao.DISLIKE);
        novaReacao(carlos, a48, Reacao.LIKE);
        novaReacao(ana, a32, Reacao.LIKE);
        novaReacao(bianca, a49, Reacao.LIKE);

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
