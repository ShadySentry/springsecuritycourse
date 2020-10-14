package net.proselyte.springsecurity.repository;

import javassist.NotFoundException;
import net.proselyte.springsecurity.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    private final String adminEmail = "admin@email.com";

    @Test
    void findByEmail() throws NotFoundException {
        User foundUser = userRepository.findByEmail(adminEmail).orElseThrow(()->new NotFoundException("no record with email: " + adminEmail));
        assertTrue(foundUser.getEmail().equals(adminEmail));
    }
}