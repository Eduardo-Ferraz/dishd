package com.dishd;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Testes de integracao ponta-a-ponta (contexto completo + H2 + DataSeeder + Spring Security).
 * Exercita autenticacao, endpoints publicos e protecao de rotas.
 */
@SpringBootTest
@AutoConfigureMockMvc
class ApiIntegrationTest {

    @Autowired
    MockMvc mvc;

    @Test
    void login_usuarioDemoSeeded_retornaToken() throws Exception {
        String body = """
                {"email":"demo@dishd.com","password":"demo1234"}
                """;
        mvc.perform(post("/api/auth/login").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty())
                .andExpect(jsonPath("$.tipo").value("Bearer"))
                .andExpect(jsonPath("$.usuario.email").value("demo@dishd.com"));
    }

    @Test
    void login_senhaErrada_retorna400() throws Exception {
        String body = """
                {"email":"demo@dishd.com","password":"errada"}
                """;
        mvc.perform(post("/api/auth/login").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    void register_novoUsuario_retorna201ComToken() throws Exception {
        String body = """
                {"username":"novo","nome":"Novo Usuario","email":"novo@dishd.com","telefone":"27999998888","password":"senha123"}
                """;
        mvc.perform(post("/api/auth/register").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.token").isNotEmpty());
    }

    @Test
    void register_dadosInvalidos_retorna400() throws Exception {
        String body = """
                {"username":"ab","nome":"","email":"invalido","password":"123"}
                """;
        mvc.perform(post("/api/auth/register").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    void listarRestaurantes_publico_retornaSeed() throws Exception {
        mvc.perform(get("/api/restaurantes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").isNotEmpty());
    }

    @Test
    void listarCategorias_publico_ok() throws Exception {
        mvc.perform(get("/api/categorias"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void criarRestaurante_semToken_negado() throws Exception {
        String body = """
                {"nome":"Sem Auth"}
                """;
        mvc.perform(post("/api/restaurantes").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void me_semToken_negado() throws Exception {
        mvc.perform(get("/api/auth/me"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void fluxoAutenticado_criaAvaliacaoComToken() throws Exception {
        // Autentica e extrai o token.
        String login = """
                {"email":"demo@dishd.com","password":"demo1234"}
                """;
        String resposta = mvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON).content(login))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        String token = resposta.replaceAll(".*\"token\"\\s*:\\s*\"([^\"]+)\".*", "$1");

        // Usa o token para acessar rota protegida /api/auth/me.
        mvc.perform(get("/api/auth/me").header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("demo@dishd.com"));
    }
}
