package org.lonelyproject.config;

import org.testcontainers.containers.PostgreSQLContainer;

public class PostgresqlContainer extends PostgreSQLContainer<PostgresqlContainer> {

    private static final String IMAGE_VERSION = "postgres:13.2";
    private static PostgresqlContainer container;

    private PostgresqlContainer() {
        super(IMAGE_VERSION);
    }

    public static PostgresqlContainer getInstance() {
        if (container == null) {
            container = new PostgresqlContainer()
                .withDatabaseName("tests")
                .withUsername("test")
                .withPassword("test");
        }
        return container;
    }

    @Override
    public void start() {
        super.start();
    }

    @Override
    public void stop() {
    }

}
