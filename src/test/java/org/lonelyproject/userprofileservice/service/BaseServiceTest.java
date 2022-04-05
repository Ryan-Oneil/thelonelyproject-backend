package org.lonelyproject.userprofileservice.service;

import org.junit.ClassRule;
import org.junit.jupiter.api.extension.ExtendWith;
import org.lonelyproject.config.PostgresqlContainer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.PostgreSQLContainer;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Sql({"/data.sql"})
@Sql(scripts = "/delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
public class BaseServiceTest {

    @ClassRule
    public static PostgreSQLContainer<PostgresqlContainer> postgreSQLContainer = PostgresqlContainer.getInstance();
}
