package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.FilmValidationException;
import ru.yandex.practicum.filmorate.exceptions.UserValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.List;

@Service
public class FilmService {
    private final FilmStorage filmStorage;

    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public void addLike(User user, Film film) {
        if (user == null) {
            throw new UserValidationException("Пользователь не может быть null");
        }
        if (film == null) {
            throw new FilmValidationException("Фильм не может быть null");
        }
        film.addLike(user);
    }

    public void removeLike(User user, Film film) {
        if (user == null) {
            throw new UserValidationException("Пользователь не может быть null");
        }
        if (film == null) {
            throw new FilmValidationException("Фильм не может быть null");
        }
        film.removeLike(user);
    }

    public List<Film> getTop10LikedFilms() {
        return filmStorage.getAllFilms().stream()
                .sorted((f1, f2) -> f2.getNumberOfLikes() - f1.getNumberOfLikes())
                .limit(10)
                .toList();
    }
}
