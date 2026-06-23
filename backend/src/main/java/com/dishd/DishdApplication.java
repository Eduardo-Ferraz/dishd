package com.dishd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Ponto de entrada da aplicacao Dishd.
 *
 * <p>A API REST sobe por padrao em {@code http://localhost:8080} usando um banco
 * H2 em memoria com dados de exemplo. A documentacao interativa fica em
 * {@code /swagger-ui.html}.</p>
 */
@SpringBootApplication
public class DishdApplication {

    public static void main(String[] args) {
        SpringApplication.run(DishdApplication.class, args);
    }
}
