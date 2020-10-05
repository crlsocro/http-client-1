package no.kristiania.database;

import org.flywaydb.core.Flyway;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

class ProductDaoTest {

    private ProductDao productDao = new ProductDao(createTestDataSource());

    @Test
    void shouldListInsertedProducts() throws SQLException {
        Product product = exampleProduct();
        productDao.insert(product);
        assertThat(productDao.list())
                .extracting(Product::getName)
                .contains(product.getName());
    }

    @Test
    void shouldRetrieveInsertedProduct() throws SQLException {
        Product product = exampleProduct();
        productDao.insert(product);
        assertThat(product).hasNoNullFieldsOrProperties();
        assertThat(productDao.retrieve(product.getId()))
                .usingRecursiveComparison()
                .isEqualTo(product);
    }


    private JdbcDataSource createTestDataSource() {
        JdbcDataSource dataSource = new JdbcDataSource();
        dataSource.setUrl("jdbc:h2:mem:testdatabase;DB_CLOSE_DELAY=-1");
        Flyway.configure().dataSource(dataSource).load().migrate();
        return dataSource;
    }

    private Product exampleProduct() {
        Product product = new Product();
        product.setName(exampleProductName());
        return product;
    }

    /** Returns a random product name */
    private String exampleProductName() {
        String[] options = {"Apples", "Bananas", "Coconuts", "Dates", "Eggplant"};
        Random random = new Random();
        return options[random.nextInt(options.length)];
    }
}