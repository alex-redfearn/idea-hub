package com.verycoolapp.ideahub;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public abstract class MySqlTestContainer {
    @Container
    public static final MySQLContainer<?> mysqlContainer = new MySQLContainer<>("mysql:8.0.26")
            .withUsername("username")
            .withPassword("password")
            .withDatabaseName("db")
            .withExposedPorts(3306);

    @DynamicPropertySource
    public static void registerMySQLProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mysqlContainer::getJdbcUrl);
        registry.add("spring.datasource.username", mysqlContainer::getUsername);
        registry.add("spring.datasource.password", mysqlContainer::getPassword);
    }

}

