package com.example.vvs.webapp.repository;

import com.example.vvs.webapp.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;

@TestPropertySource(locations="classpath:application-test.properties")
@ActiveProfiles("test")
@SpringBootTest
public class ProductRepoTest {

    @Autowired
    private ProductRepo repo;

    @BeforeEach
    private void before()
    {
        repo.deleteAll();
    }

    @Test
    void saveInRepository_thenTest_getByNameMethod()
    {
        Product product = new Product();

        product.setName("test");
        product.setDescription("descriere_test");
        product.setPhoto("test");
        product.setPrice(323);

        repo.save(product);

        assertThat(repo.getByName("test")).isNotNull();
        assertThat(repo.count()).isEqualTo(1);
        assertThat(repo.getByName("test").getName()).isEqualTo("test");
        assertThat(repo.getByName("test").getDescription()).isEqualTo("descriere_test");
        assertThat(repo.getByName("test").getPhoto()).isEqualTo("test");
        assertThat(repo.getByName("test").getPrice()).isEqualTo(323);
    }

    @Test
    void test_getByNameWithNoSavedEntity()
    {
        assertThat(repo.getByName("test123")).isNull();
    }
}
