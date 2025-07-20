package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import ru.yandex.practicum.filmorate.exceptions.UserValidationException;

import java.time.LocalDate;
import java.util.Set;

@Data
public class Film {
    private int id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private int duration;
    private Set<User> usersWhoLiked;

    public void addLike(User user) {
        if (user == null) {
            throw new UserValidationException("Пользователь не может быть null.");
        }
        this.usersWhoLiked.add(user);
    }

    public void removeLike(User user) {
        if (user == null) {
            throw new UserValidationException("Пользователь не может быть null.");
        }
        this.usersWhoLiked.remove(user);
    }

    public int getNumberOfLikes() {
        return this.usersWhoLiked.size();
    }
}
