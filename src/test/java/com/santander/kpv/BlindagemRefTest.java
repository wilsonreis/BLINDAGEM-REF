package com.santander.kpv;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class BlindagemRefTest {

    @Test
    void contextLoads() {
        // Testa se o contexto do Spring Boot carrega sem problemas
    }

    @Test
    void testMain() {
        // Testa a execução do método main
        assertDoesNotThrow(() -> BlindagemRef.main(new String[] {}));
    }
}
