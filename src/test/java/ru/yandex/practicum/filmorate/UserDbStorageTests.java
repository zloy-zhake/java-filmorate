package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserDbStorage;
import ru.yandex.practicum.filmorate.storage.mappers.UserRowMapper;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({UserDbStorage.class, UserRowMapper.class})
public class UserDbStorageTests {

    private final UserDbStorage userDbStorage;

    @Test
    public void testGetAllUsers() {
        User testUser1 = new User();
        testUser1.setEmail("user1@user.com");
        testUser1.setLogin("login1");
        testUser1.setName("user1");
        testUser1.setBirthday(LocalDate.of(2000, 1, 1));
        testUser1.setFriends(new ArrayList<>());
        User createdUser1 = userDbStorage.createUser(testUser1);
        testUser1.setId(createdUser1.getId());
        User testUser2 = new User();
        testUser2.setEmail("user2@user.com");
        testUser2.setLogin("login2");
        testUser2.setName("user2");
        testUser2.setBirthday(LocalDate.of(2000, 1, 1));
        testUser2.setFriends(new ArrayList<>());
        User createdUser2 = userDbStorage.createUser(testUser2);
        testUser2.setId(createdUser2.getId());
        List<User> users = userDbStorage.getAllUsers();
        assertThat(users).hasSize(2)
                .contains(testUser1)
                .contains(testUser2);
    }

    @Test
    public void testCreateUser() {
        User testUser = new User();
        testUser.setEmail("user@user.com");
        testUser.setLogin("login");
        testUser.setName("user");
        testUser.setBirthday(LocalDate.of(2000, 1, 1));
        testUser.setFriends(new ArrayList<>());
        User createdUser = userDbStorage.createUser(testUser);
        testUser.setId(createdUser.getId());
        Optional<User> userOptional = userDbStorage.getUserById(testUser.getId());
        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).isEqualTo(testUser)
                );
    }

    @Test
    public void testGetUserByEmail() {
        User testUser = new User();
        testUser.setEmail("user@user.com");
        testUser.setLogin("login");
        testUser.setName("user");
        testUser.setBirthday(LocalDate.of(2000, 1, 1));
        User createdUser = userDbStorage.createUser(testUser);
        testUser.setId(createdUser.getId());
        Optional<User> userOptional = userDbStorage.getUserByEmail(testUser.getEmail());
        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).isEqualTo(testUser)
                );
    }

    @Test
    public void testGetUserByLogin() {
        User testUser = new User();
        testUser.setEmail("user@user.com");
        testUser.setLogin("login");
        testUser.setName("user");
        testUser.setBirthday(LocalDate.of(2000, 1, 1));
        User createdUser = userDbStorage.createUser(testUser);
        testUser.setId(createdUser.getId());
        Optional<User> userOptional = userDbStorage.getUserByLogin(testUser.getLogin());
        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).isEqualTo(testUser)
                );
    }

    @Test
    public void testUpdateUser() {
        User testUser = new User();
        testUser.setEmail("user@user.com");
        testUser.setLogin("login");
        testUser.setName("user");
        testUser.setBirthday(LocalDate.of(2000, 1, 1));
        User createdUser = userDbStorage.createUser(testUser);
        testUser.setId(createdUser.getId());
        testUser.setName("updated user");
        userDbStorage.updateUser(testUser);
        Optional<User> userOptional = userDbStorage.getUserById(testUser.getId());
        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("name", "updated user")
                );
    }

    @Test
    public void testAddFriend() {
        User testUser1 = new User();
        testUser1.setEmail("user1@user.com");
        testUser1.setLogin("login1");
        testUser1.setName("user1");
        testUser1.setBirthday(LocalDate.of(2000, 1, 1));
        testUser1.setFriends(new ArrayList<>());
        User createdUser1 = userDbStorage.createUser(testUser1);
        testUser1.setId(createdUser1.getId());

        User testUser2 = new User();
        testUser2.setEmail("user2@user.com");
        testUser2.setLogin("login2");
        testUser2.setName("user2");
        testUser2.setBirthday(LocalDate.of(2000, 1, 1));
        testUser2.setFriends(new ArrayList<>());
        User createdUser2 = userDbStorage.createUser(testUser2);
        testUser2.setId(createdUser2.getId());

        userDbStorage.addFriend(testUser1.getId(), testUser2.getId());
        List<User> friends = userDbStorage.getUserFriends(testUser1.getId());
        assertThat(friends).hasSize(1).contains(testUser2);
    }

    @Test
    public void testRemoveFriend() {
        User testUser1 = new User();
        testUser1.setEmail("user1@user.com");
        testUser1.setLogin("login1");
        testUser1.setName("user1");
        testUser1.setBirthday(LocalDate.of(2000, 1, 1));
        testUser1.setFriends(new ArrayList<>());
        User createdUser1 = userDbStorage.createUser(testUser1);
        testUser1.setId(createdUser1.getId());

        User testUser2 = new User();
        testUser2.setEmail("user2@user.com");
        testUser2.setLogin("login2");
        testUser2.setName("user2");
        testUser2.setBirthday(LocalDate.of(2000, 1, 1));
        testUser2.setFriends(new ArrayList<>());
        User createdUser2 = userDbStorage.createUser(testUser2);
        testUser2.setId(createdUser2.getId());

        userDbStorage.addFriend(testUser1.getId(), testUser2.getId());
        List<User> friendsBefore = userDbStorage.getUserFriends(testUser1.getId());
        assertThat(friendsBefore).hasSize(1).contains(testUser2);

        userDbStorage.removeFriend(testUser1.getId(), testUser2.getId());
        List<User> friendsAfter = userDbStorage.getUserFriends(testUser1.getId());
        assertThat(friendsAfter).hasSize(0);
    }
}
