package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserStorage {
    User createUser(User newUser);

    User updateUser(User updatedUser);

    List<User> getAllUsers();

    Optional<User> getUserById(int id);

    Optional<User> getUserByEmail(String email);

    Optional<User> getUserByLogin(String login);

    void addFriend(int user1id, int user2id);

    List<User> getUserFriends(int userId);
}
