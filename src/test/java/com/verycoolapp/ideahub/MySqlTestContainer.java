package com.verycoolapp.ideahub;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public abstract class MySqlTestContainer {

    public static final MySQLContainer<?> MY_SQL_CONTAINER = new MySQLContainer<>("mysql:8.0.26")
            .withUsername("username")
            .withPassword("password")
            .withDatabaseName("db")
            .withExposedPorts(3306);

    @DynamicPropertySource
    public static void registerMySQLProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", MY_SQL_CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.username", MY_SQL_CONTAINER::getUsername);
        registry.add("spring.datasource.password", MY_SQL_CONTAINER::getPassword);
    }

    static {
        MY_SQL_CONTAINER.start();
    }


}

