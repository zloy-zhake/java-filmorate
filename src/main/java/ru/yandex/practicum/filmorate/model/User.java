package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import ru.yandex.practicum.filmorate.exceptions.UserValidationException;

import java.time.LocalDate;
import java.util.Set;

@Data
public class User {
    private int id;
    private String email;
    private String login;
    private String name;
    private LocalDate birthday;
    private Set<User> friends;

    public void addFriend(User newFriend) {
        if (newFriend == null) {
            throw new UserValidationException("Пользователь не может быть null.");
        }
        this.friends.add(newFriend);
    }

    public void removeFriend(User user) {
        this.friends.remove(user);
    }
}
