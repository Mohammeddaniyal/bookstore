package com.daniyal.bookstore.repository;

import com.daniyal.bookstore.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;


import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;
    @Test
    public void testSaveAndFindByUsername()
    {
        //create new user
        User user=User.builder()
                .username("hell124")
                .password("tttt1234")
                .email("ashgreninja@gmail.com")
                .build();
        // save user in db
         userRepository.save(user);

         // find by username
        Optional<User> found=userRepository.findByUsername("hell124");

        // assert user is present and fields match

        assertThat(found).isPresent();
        assertThat(found.get().getEmail()).isEqualTo("ashgreninja@gmail.com");
    }
}
