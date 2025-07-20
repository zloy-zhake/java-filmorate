package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.List;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final FilmStorage filmStorage;

    public FilmController(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    @PostMapping
    public Film addFilm(@RequestBody Film newFilm) {
        return this.filmStorage.addFilm(newFilm);
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film updatedFilm) {
        return this.filmStorage.updateFilm(updatedFilm);
    }

    @GetMapping
    public List<Film> getAllFilms() {
        return this.filmStorage.getAllFilms();
    }

    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable int id) {
        Film film = filmStorage.getFilmById(id);
        if (film == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Фильм с указанным id не найден."
            );
        }
        return film;
    }
}
