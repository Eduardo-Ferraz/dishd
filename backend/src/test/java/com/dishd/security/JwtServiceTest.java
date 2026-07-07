package com.dishd.security;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

/** Testes de geracao e validacao de JWT ({@link JwtService}). */
class JwtServiceTest {

    private static final String SECRET = "chave-secreta-de-teste-para-jwt-hs256-0123456789";
    private static final long UMA_HORA = 3_600_000L;

    @Test
    void geraToken_valido_eExtraiEmail() {
        JwtService jwt = new JwtService(SECRET, UMA_HORA);

        String token = jwt.generateToken("demo@dishd.com", 7L);

        assertThat(jwt.isValid(token)).isTrue();
        assertThat(jwt.extractEmail(token)).isEqualTo("demo@dishd.com");
    }

    @Test
    void tokenMalformado_ehInvalido() {
        JwtService jwt = new JwtService(SECRET, UMA_HORA);

        assertThat(jwt.isValid("isto-nao-e-um-jwt")).isFalse();
    }

    @Test
    void tokenAdulterado_ehInvalido() {
        JwtService jwt = new JwtService(SECRET, UMA_HORA);
        String token = jwt.generateToken("demo@dishd.com", 7L);

        // Muta o primeiro caractere do payload -> assinatura nao confere.
        char[] chars = token.toCharArray();
        int payloadStart = token.indexOf('.') + 1;
        chars[payloadStart] = chars[payloadStart] == 'A' ? 'B' : 'A';
        String adulterado = new String(chars);

        assertThat(jwt.isValid(adulterado)).isFalse();
    }

    @Test
    void tokenExpirado_ehInvalido() {
        JwtService jwtExpirado = new JwtService(SECRET, -1_000L); // expira no passado

        String token = jwtExpirado.generateToken("demo@dishd.com", 7L);

        assertThat(jwtExpirado.isValid(token)).isFalse();
    }

    @Test
    void tokenDeOutraChave_ehInvalido() {
        JwtService emissor = new JwtService(SECRET, UMA_HORA);
        JwtService outraChave = new JwtService("outra-chave-secreta-para-jwt-hs256-987654321-abc", UMA_HORA);
        String token = emissor.generateToken("demo@dishd.com", 7L);

        assertThat(outraChave.isValid(token)).isFalse();
    }
}
