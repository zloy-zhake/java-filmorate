package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exceptions.UserValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class UserValidationTests {
    UserController uc;
    User testUser;

    @BeforeEach
    void setUp() {
        uc = new UserController();
        testUser = new User();
        testUser.setId(1);
        testUser.setEmail("name@email.com");
        testUser.setLogin("login");
        testUser.setName("Name");
        testUser.setBirthday(LocalDate.of(2000, 1, 1));
    }

    @Test
    void testValidUser() {
        assertDoesNotThrow(() -> uc.createUser(testUser));
    }

    @Test
    void testNoEmail() {
        testUser.setEmail("");
        assertThrows(UserValidationException.class, () -> uc.createUser(testUser));
    }

    @Test
    void testEmailWithoutAt() {
        testUser.setEmail("name");
        assertThrows(UserValidationException.class, () -> uc.createUser(testUser));
    }

    @Test
    void testNoLogin() {
        testUser.setLogin("");
        assertThrows(UserValidationException.class, () -> uc.createUser(testUser));
    }

    @Test
    void testLoginWithSpace() {
        testUser.setLogin("log in");
        assertThrows(UserValidationException.class, () -> uc.createUser(testUser));
    }

    @Test
    void testNoName() {
        testUser.setName("");
        User createdUser = uc.createUser(testUser);
        assertEquals(createdUser.getName(), createdUser.getLogin());
    }

    @Test
    void testFakeBirthday() {
        testUser.setBirthday(LocalDate.of(3000, 1, 1));
        assertThrows(UserValidationException.class, () -> uc.createUser(testUser));
    }
}
