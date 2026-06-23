# Dishd — Backend (API REST)

API REST do Dishd em **Spring Boot 3 / Java 17**, com autenticação **JWT**,
persistência **JPA/Hibernate** e documentação **Swagger**.

## ▶️ Como executar

### Modo rápido (H2 em memória — zero configuração)

```bash
cd backend
./mvnw spring-boot:run        # no Windows: mvnw.cmd spring-boot:run
```

A API sobe em <http://localhost:8080> já com **dados de exemplo**. Pronto para o
frontend consumir. Nenhum banco precisa estar instalado.

- 📘 Documentação interativa (Swagger UI): <http://localhost:8080/swagger-ui.html>
- 🗄️ Console do banco H2: <http://localhost:8080/h2-console> (JDBC URL: `jdbc:h2:mem:dishd`, usuário `sa`, sem senha)
- ❤️ Health check: <http://localhost:8080/actuator/health>

**Usuário de teste já cadastrado:** `demo@dishd.com` / senha `demo1234`.

### Com PostgreSQL (perfil `postgres`)

```bash
cd backend
./mvnw spring-boot:run -Dspring-boot.run.profiles=postgres
```

Variáveis (com valores padrão): `DB_HOST=localhost`, `DB_PORT=5432`, `DB_NAME=dishd`,
`DB_USER=dishd`, `DB_PASSWORD=dishd`. O jeito mais fácil de subir o Postgres + a API
juntos é via Docker Compose, na raiz do repositório:

```bash
docker compose up --build
```

## 🔑 Autenticação (fluxo para o frontend)

1. `POST /api/auth/register` ou `POST /api/auth/login` → resposta traz `token`.
2. Enviar o token nas próximas requisições no cabeçalho:
   `Authorization: Bearer <token>`.

Leituras (métodos `GET`) são públicas; criar/editar/excluir e reagir exigem token.

## 🌐 Endpoints

| Método | Rota | Protegido | Descrição |
|---|---|:---:|---|
| POST | `/api/auth/register` | | Cadastro (retorna token) |
| POST | `/api/auth/login` | | Login (retorna token) |
| GET | `/api/auth/me` | 🔒 | Usuário autenticado |
| GET | `/api/usuarios` | | Lista usuários |
| GET | `/api/usuarios/{id}` | | Perfil do usuário |
| GET | `/api/usuarios/{id}/avaliacoes` | | Diário (avaliações) do usuário |
| GET | `/api/usuarios/{id}/estatisticas` | | Estatísticas do usuário |
| GET | `/api/restaurantes?busca=&categoriaId=` | | Lista/filtra restaurantes |
| GET | `/api/restaurantes/{id}` | | Detalhe do restaurante |
| GET | `/api/restaurantes/{id}/avaliacoes` | | Avaliações do restaurante |
| POST | `/api/restaurantes` | 🔒 | Cria restaurante |
| PUT | `/api/restaurantes/{id}` | 🔒 | Atualiza restaurante |
| DELETE | `/api/restaurantes/{id}` | 🔒 | Remove restaurante |
| GET | `/api/categorias` | | Lista categorias |
| GET | `/api/categorias/{id}` | | Detalhe da categoria |
| POST | `/api/categorias` | 🔒 | Cria categoria |
| GET | `/api/avaliacoes?page=&size=` | | Feed social (paginado) |
| GET | `/api/avaliacoes/{id}` | | Detalhe da avaliação |
| POST | `/api/avaliacoes` | 🔒 | Cria avaliação |
| PUT | `/api/avaliacoes/{id}` | 🔒 | Atualiza avaliação própria |
| DELETE | `/api/avaliacoes/{id}` | 🔒 | Remove avaliação própria |
| POST | `/api/avaliacoes/{id}/reacoes` | 🔒 | Curte/descurte (`{"tipo":"LIKE"}`) |
| DELETE | `/api/avaliacoes/{id}/reacoes` | 🔒 | Remove a própria reação |

> A maneira mais completa de ver os contratos (campos de cada DTO, exemplos e
> respostas) é pelo **Swagger UI**.

## 🧪 Testes

```bash
cd backend
./mvnw test
```

## 📚 Gerar a documentação do código (JavaDoc)

```bash
cd backend
./mvnw javadoc:javadoc
```

Saída em `backend/target/site/apidocs/index.html`.

## 🗂️ Estrutura

```text
backend/src/main/java/com/dishd/
├── config/      # Segurança, OpenAPI/Swagger e carga de dados (DataSeeder)
├── controller/  # Endpoints REST
├── domain/      # Entidades JPA (Usuario, Restaurante, Avaliacao, ...)
├── dto/         # Objetos de transferência (records)
├── exception/   # Exceções e tratamento global de erros
├── repository/  # Spring Data JPA
├── security/    # JWT (geração, filtro e usuário atual)
└── service/     # Regras de negócio
```

## 🧰 Tecnologias

Spring Boot 3.4 · Spring Web · Spring Data JPA / Hibernate · Spring Security ·
JJWT · H2 (dev) · PostgreSQL (prod) · springdoc-openapi (Swagger) · Maven · JUnit 5.
