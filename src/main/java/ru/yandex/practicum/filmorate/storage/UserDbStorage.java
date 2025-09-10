package ru.yandex.practicum.filmorate.storage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

@Repository
public class UserDbStorage extends BaseBdStorage<User> implements UserStorage {
    private static final String GET_ALL_QUERY = "SELECT * FROM users";
    private static final String GET_BY_ID_QUERY = "SELECT * FROM users WHERE id = ?";
    private static final String GET_BY_EMAIL_QUERY = "SELECT * FROM users WHERE email = ?";
    private static final String GET_BY_LOGIN_QUERY = "SELECT * FROM users WHERE login = ?";
    private static final String UPDATE_QUERY =
            "UPDATE users " +
                    "SET email = ?, login = ?, name = ?, birthday = ? " +
                    "WHERE id = ?";
    private static final String INSERT_QUERY =
            "INSERT INTO users(email, login, name, birthday)" +
                    "VALUES (?, ?, ?, ?)";

    public UserDbStorage(JdbcTemplate jdbc, RowMapper<User> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public List<User> getAllUsers() {
        return findMany(GET_ALL_QUERY);
    }

    @Override
    public Optional<User> getUserById(int id) {
        return findOne(GET_BY_ID_QUERY, id);
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        return findOne(GET_BY_EMAIL_QUERY, email);
    }

    @Override
    public Optional<User> getUserByLogin(String login) {
        return findOne(GET_BY_LOGIN_QUERY, login);
    }

    @Override
    public User createUser(User newUser) {
        int id = insert(
                INSERT_QUERY,
                newUser.getEmail(),
                newUser.getLogin(),
                newUser.getName(),
                newUser.getBirthday()
        );
        newUser.setId(id);
        return newUser;
    }

    @Override
    public User updateUser(User user) {
        update(
                UPDATE_QUERY,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId()
        );
        return user;
    }
}
