package org.lonelyproject.userprofileservice.repository;

import org.junit.ClassRule;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.lonelyproject.config.PostgresqlContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.PostgreSQLContainer;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@Sql({"/data.sql"})
@Sql(scripts = "/delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
public class BaseRepository {

    @Autowired
    protected TestEntityManager entityManager;

    @ClassRule
    public static PostgreSQLContainer<PostgresqlContainer> postgreSQLContainer = PostgresqlContainer.getInstance();

    @AfterEach
    public void cleanup() {
        entityManager.clear();
    }

}
