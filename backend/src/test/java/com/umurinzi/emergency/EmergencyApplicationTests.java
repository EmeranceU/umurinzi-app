package com.umurinzi.emergency;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

/**
 * Smoke test for the Phase 0 scaffold: the application context must start and every
 * Flyway migration (V1-V5) must apply cleanly against a real Postgres instance.
 * Deliberately the only test in this phase — no business logic exists yet to test
 * beyond "does the schema this SDD describes actually build."
 */
@Testcontainers
@SpringBootTest
class EmergencyApplicationTests {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

    @DynamicPropertySource
    static void datasourceProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Test
    void contextLoadsAndMigrationsApply() {
        // Intentionally empty: a failing Flyway migration or a JPA entity/schema
        // mismatch (ddl-auto: validate) fails context startup before this runs.
    }
}
