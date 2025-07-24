package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.config.AppConfig;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

@Service
public class FilmService {
    private final AppConfig appConfig;
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public FilmService(
            AppConfig appConfig, FilmStorage filmStorage, UserStorage userStorage
    ) {
        this.appConfig = appConfig;
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public void addLike(int userId, int filmId) {
        User user = this.userStorage.getUserById(userId);
        Film film = this.filmStorage.getFilmById(filmId);
        film.addLike(user);
    }

    public void removeLike(int userId, int filmId) {
        User user = this.userStorage.getUserById(userId);
        Film film = this.filmStorage.getFilmById(filmId);
        film.removeLike(user);
    }

    public List<Film> getTopLikedFilms(Integer count) {
        if (count == null) {
            count = appConfig.getDefaultNumberOfTopFilms();
        }
        return filmStorage.getAllFilms().stream()
                .sorted((f1, f2) -> f2.getNumberOfLikes() - f1.getNumberOfLikes())
                .limit(count)
                .toList();
    }
}
