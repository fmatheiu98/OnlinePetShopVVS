package com.example.vvs.webapp.repository;

import com.example.vvs.webapp.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
class UserRepoTest {

    @Autowired
    private UserRepo repo;

    @Test
    void saveInRepository_thenTest_findByEmail()
    {
        User user = new User();

        user.setFirst_name("test_first_name");
        user.setLast_name("test_last_name");
        user.setEmail("test_email");
        user.setPasswd("test_passwd");

        repo.save(user);

        assertThat(repo.findByEmail("test_email")).isNotNull();
        assertThat(repo.count()).isEqualTo(1);
        assertThat(repo.findByEmail("test_email").getFirst_name()).isEqualTo("test_first_name");
        assertThat(repo.findByEmail("test_email").getLast_name()).isEqualTo("test_last_name");
        assertThat(repo.findByEmail("test_email").getEmail()).isEqualTo("test_email");
        assertThat(repo.findByEmail("test_email").getPasswd()).isEqualTo("test_passwd");
    }

    @Test
    void test_getByNameWithNoSavedEntity()
    {
        assertThat(repo.findByEmail("myEmail")).isNull();
    }
}