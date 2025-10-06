package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.config.AppConfig;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.dto.NewFilmRequest;
import ru.yandex.practicum.filmorate.dto.UpdateFilmRequest;
import ru.yandex.practicum.filmorate.exceptions.DuplicatedDataException;
import ru.yandex.practicum.filmorate.exceptions.FilmValidationException;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FilmService {
    private final AppConfig appConfig;
    private final FilmStorage filmStorage;

    public FilmService(
            AppConfig appConfig,
            @Qualifier("filmDbStorage") FilmStorage filmStorage
    ) {
        this.appConfig = appConfig;
        this.filmStorage = filmStorage;
    }

    public List<FilmDto> getAllFilms() {
        return this.filmStorage.getAllFilms().stream()
                .map(FilmMapper::mapToFilmDto)
                .collect(Collectors.toList());
    }

    public FilmDto addFilm(NewFilmRequest newFilmRequest) {
        validateNewFilmRequest(newFilmRequest);
        if (filmStorage.filmExists(newFilmRequest.getName(), newFilmRequest.getReleaseDate(), newFilmRequest.getDuration())) {
            throw new DuplicatedDataException("Данный фильм уже имется в БД: " + newFilmRequest);
        }
        Film newFilm = FilmMapper.mapToFilm(newFilmRequest);
        newFilm = filmStorage.addFilm(newFilm);
        return FilmMapper.mapToFilmDto(newFilm);
    }

    public FilmDto updateFilm(UpdateFilmRequest updateFilmRequest) {
        Film updatedFilm = filmStorage.getFilmById(updateFilmRequest.getId())
                .map(film -> FilmMapper.updateFilmFields(film, updateFilmRequest))
                .orElseThrow(() -> new NoSuchElementException(
                        "Фильма с ID=" + updateFilmRequest.getId() + "нет в БД."
                ));
        updatedFilm = filmStorage.updateFilm(updatedFilm);
        return FilmMapper.mapToFilmDto(updatedFilm);
    }

    public void addLike(int userId, int filmId) {
        filmStorage.addLike(userId, filmId);
    }

    public void removeLike(int userId, int filmId) {
        filmStorage.removeLike(userId, filmId);
    }

    public List<FilmDto> getTopLikedFilms(Integer count) {
        if (count == null) {
            count = appConfig.getDefaultNumberOfTopFilms();
        }
        return filmStorage.getTopLikedFilmIds(count).stream()
                .map(filmId -> filmStorage.getFilmById(filmId).get())
                .map(FilmMapper::mapToFilmDto)
                .toList();
    }

    public List<FilmDto> getFilmsWithGenre(int genreId) {
        return filmStorage.getFilmsWithGenre(genreId).stream()
                .map(FilmMapper::mapToFilmDto)
                .toList();
    }

    private void validateNewFilmRequest(NewFilmRequest newFilmRequest) throws FilmValidationException {
        if (newFilmRequest.getName() == null || newFilmRequest.getName().isBlank()) {
            log.error("Название фильма отсутствует. {}", newFilmRequest);
            throw new FilmValidationException("Название фильма не может быть пустым.");
        }
        if (newFilmRequest.getDescription().length() > 200) {
            log.error(
                    "Длина описания фильма ({}) превышает 200 символов. {}",
                    newFilmRequest.getDescription().length(),
                    newFilmRequest
            );
            throw new FilmValidationException(
                    "Максимальная длина описания фильма не может превышать 200 символов."
            );
        }
        LocalDate earliestReleaseDAte = LocalDate.of(1895, 12, 28);
        if (newFilmRequest.getReleaseDate().isBefore(earliestReleaseDAte)) {
            log.error("Указана нереалистичная дата релиза. {}", newFilmRequest);
            throw new FilmValidationException(
                    "Дата релиза не может быть раньше 28 декабря 1895 года."
            );
        }
        if (newFilmRequest.getDuration() < 0) {
            log.error("Указана нереалистичная продолжительность фильма. {}", newFilmRequest);
            throw new FilmValidationException(
                    "Продолжительность фильма должна быть положительным числом."
            );
        }
        Map<String, Integer> mpaRatingItem = newFilmRequest.getMpa();
        if (!filmStorage.mpaRatingExists(mpaRatingItem.get("id"))) {
            throw new NoSuchElementException("В БД нет рейтинга MPA с ID=" + mpaRatingItem.get("id"));
        }
        List<Map<String, Integer>> genreItems = newFilmRequest.getGenres();
        for (Map<String, Integer> genreItem : genreItems) {
            if (!filmStorage.genreExists(genreItem.get("id"))) {
                throw new NoSuchElementException("В БД нет жанра с ID=" + genreItem.get("id"));
            }
        }
    }
}
