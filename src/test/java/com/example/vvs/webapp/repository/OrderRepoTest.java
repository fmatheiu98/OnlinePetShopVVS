package com.example.vvs.webapp.repository;

import com.example.vvs.webapp.model.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest
class OrderRepoTest {

    @Autowired
    private OrderRepo repo;

    @Test
    void saveInRepository_thenTestItWasInserted()
    {
        Order order = new Order();

        order.setFirst_name("test_first_name");
        order.setLast_name("test_last_name");
        order.setEmail("test_email");
        order.setCountry("test_country");
        order.setAddress("test_address");

        repo.save(order);

        assertThat(repo.findAll().get(0)).isNotNull();
        assertThat(repo.count()).isEqualTo(1);
        assertThat(repo.findAll().get(0).getFirst_name()).isEqualTo("test_first_name");
        assertThat(repo.findAll().get(0).getLast_name()).isEqualTo("test_last_name");
        assertThat(repo.findAll().get(0).getEmail()).isEqualTo("test_email");
        assertThat(repo.findAll().get(0).getCountry()).isEqualTo("test_country");
        assertThat(repo.findAll().get(0).getAddress()).isEqualTo("test_address");
    }

    @Test
    void testRepoWithoutSavedEntities()
    {
        assertThat(repo.count()).isEqualTo(0);
        assertThat(repo.findAll()).isEmpty();
    }
}