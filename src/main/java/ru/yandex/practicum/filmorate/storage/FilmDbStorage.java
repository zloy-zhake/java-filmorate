package ru.yandex.practicum.filmorate.storage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public class FilmDbStorage extends BaseBdStorage<Film> implements FilmStorage {
    private static final String INSERT_QUERY =
            "INSERT INTO films(name, description, releaseDate, duration)" +
                    "VALUES (?, ?, ?, ?)";

    public FilmDbStorage(JdbcTemplate jdbc, RowMapper<Film> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public Film addFilm(Film newFilm) {
        int id = insert(
                INSERT_QUERY,
                newFilm.getName(),
                newFilm.getDescription(),
                newFilm.getReleaseDate(),
                newFilm.getDuration()
        );
        newFilm.setId(id);
        return newFilm;
    }

    @Override
    public Film updateFilm(Film updatedFilm) {
        return null;
    }

    @Override
    public List<Film> getAllFilms() {
        return List.of();
    }

    @Override
    public Film getFilmById(int id) {
        return null;
    }
}
