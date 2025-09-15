package ru.yandex.practicum.filmorate.storage.mappers;

import ru.yandex.practicum.filmorate.model.Film;

import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class FilmRowMapper implements RowMapper<Film> {
    @Override
    public Film mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Film film = new Film();
        film.setId(resultSet.getInt("id"));
        film.setName(resultSet.getString("name"));
        film.setDescription(resultSet.getString("description"));
        film.setReleaseDate(LocalDate.from(
                resultSet.getTimestamp("releaseDate").toLocalDateTime())
        );
        film.setDuration(resultSet.getInt("duration"));
        return film;
    }
}
