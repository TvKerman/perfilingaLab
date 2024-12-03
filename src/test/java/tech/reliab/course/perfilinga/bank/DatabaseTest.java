package tech.reliab.course.perfilinga.bank;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import tech.reliab.course.perfilinga.bank.repository.BankRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers
@SpringBootTest(classes = PerfilinLabApplication.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PostgresContainer {

    private static final String IMAGE_VERSION = "postgres:15";

    @Container
    public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>(DockerImageName.parse(IMAGE_VERSION))
            .withDatabaseName("test")
            .withUsername("postgres")
            .withPassword("password");

    static {
        postgreSQLContainer.start();
    }

    @DynamicPropertySource
    static void postgresqlProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
        registry.add("spring.datasource.driver-class-name", postgreSQLContainer::getDriverClassName);
    }
}

public class DatabaseTest extends PostgresContainer {
    @Autowired
    private BankRepository bankRepository;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void testDatabaseConnection() {
        assertNotNull(bankRepository);
    }

    @Test
    public void testTableExists() {
        String sql = "SELECT EXISTS (SELECT 1 FROM information_schema.tables WHERE table_schema = 'public' AND table_name = 'banks');";
        Boolean tableExists = jdbcTemplate.queryForObject(sql, Boolean.class);

        if (tableExists == null) {
            System.out.println("Ошибка: Не удалось получить информацию о таблице.");
        } else {
            assertTrue(tableExists, "Таблица 'banks' не существует в базе данных");
        }
    }

    @Test
    public void testTableColumns() {
        String tableName = "banks";
        String sqlCheckTableExists = "SELECT EXISTS (SELECT 1 FROM information_schema.tables WHERE table_schema = 'public' AND table_name = ?)";

        Boolean tableExists = jdbcTemplate.queryForObject(sqlCheckTableExists, Boolean.class, tableName);

        if (tableExists != null && tableExists) {
            System.out.println("Таблица 'banks' существует. Извлекаем названия колонок...");

            String sqlGetColumns = "SELECT column_name FROM information_schema.columns WHERE table_schema = 'public' AND table_name = ?";

            List<String> columnNames = jdbcTemplate.queryForList(sqlGetColumns, String.class, tableName);

            System.out.println("Колонки таблицы 'banks':");
            for (String columnName : columnNames) {
                System.out.println(columnName);
            }
        } else {
            System.out.println("Таблица 'banks' не существует.");
        }
    }
}