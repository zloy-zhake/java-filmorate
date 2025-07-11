package ru.yandex.practicum.filmorate.controller;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.FilmValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final Map<Integer, Film> films = new HashMap<>();
    @Getter
    @Setter
    private int currentId = 0;

    @PostMapping
    public Film addFilm(@RequestBody Film newFilm) {
        this.validateFilm(newFilm);
        newFilm.setId(this.getNextId());
        this.films.put(newFilm.getId(), newFilm);
        log.info("Добавлен фильм: {}", newFilm);
        return newFilm;
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film updatedFilm) {
        if (!this.films.containsKey(updatedFilm.getId())) {
            throw new NoSuchElementException("Фильма с id=" + updatedFilm.getId() + " нет в системе.");
        }
        this.validateFilm(updatedFilm);
        this.films.put(updatedFilm.getId(), updatedFilm);
        log.info("Обновлены данные о фильме: {}", updatedFilm);
        return updatedFilm;
    }

    @GetMapping
    public List<Film> getAllFilms() {
        return new ArrayList<>(this.films.values());
    }

    private int getNextId() {
        int nextId = this.getCurrentId() + 1;
        this.setCurrentId(nextId);
        return nextId;
    }

    private void validateFilm(Film film) throws FilmValidationException {
        if (film.getName() == null || film.getName().isBlank()) {
            log.error("Название фильма отсутствует. {}", film);
            throw new FilmValidationException("Название фильма не может быть пустым.");
        }
        if (film.getDescription().length() > 200) {
            log.error("Длина описания фильма ({}) превышает 200 символов. {}", film.getDescription().length(), film);
            throw new FilmValidationException("Максимальная длина описания фильма не может превышать 200 символов.");
        }
        LocalDate earliestReleaseDAte = LocalDate.of(1895, 12, 28);
        if (film.getReleaseDate().isBefore(earliestReleaseDAte)) {
            log.error("Указана нереалистичная дата релиза. {}", film);
            throw new FilmValidationException("Дата релиза не может быть раньше 28 декабря 1895 года.");
        }
        if (film.getDuration() < 0) {
            log.error("Указана нереалистичная продолжительность фильма. {}", film);
            throw new FilmValidationException("Продолжительность фильма должна быть положительным числом.");
        }
    }
}
