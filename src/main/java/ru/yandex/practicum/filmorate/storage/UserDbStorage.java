package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.mappers.UserRowMapper;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbc;
    private final UserRowMapper mapper;

    @Override
    public List<User> getAllUsers() {
        String query = "SELECT * FROM users";
        return jdbc.query(query, mapper);
    }


    @Override
    public User createUser(User newUser) {
        return null;
    }

    @Override
    public User updateUser(User updatedUser) {
        return null;
    }


    @Override
    public User getUserById(int id) {
        return null;
    }
}
