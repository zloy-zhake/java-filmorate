package ru.yandex.practicum.filmorate.storage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class FilmDbStorage extends BaseBdStorage<Film> implements FilmStorage {
    private static final String INSERT_FILM_QUERY =
            "INSERT INTO films(name, description, releaseDate, duration) " +
                    "VALUES (?, ?, ?, ?)";
    private static final String INSERT_GENRE_QUERY =
            "INSERT INTO filmsGenres(filmId, genreId) " +
                    "VALUES (?, ?)";
    private static final String INSERT_MPA_QUERY =
            "INSERT INTO filmsMpaRatings(filmId, ratingId) " +
                    "VALUES (?, ?)";
    private static final String CHECK_FOR_MPA_QUERY =
            "SELECT COUNT(*) FROM mpaRatings " +
                    "WHERE id = ?";
    private static final String CHECK_FOR_GENRE_QUERY =
            "SELECT COUNT(*) FROM genres " +
                    "WHERE id = ?";
    private static final String CHECK_FOR_FILM_QUERY =
            "SELECT COUNT(*) FROM films " +
                    "WHERE name = ? " +
                    "AND releaseDate = ? " +
                    "AND duration = ?";
    private static final String GET_FILM_BY_ID =
            "SELECT * from films WHERE id = ?;";
    private static final String GET_MPA_RATING_BY_FILM_ID =
            "SELECT ratingId from filmsMpaRatings WHERE filmId = ?;";
    private static final String GET_GENRES_BY_FILM_ID =
            "SELECT genreId from filmsGenres WHERE filmId = ?;";
    private static final String UPDATE_FILM =
            "UPDATE films " +
                    "SET name = ?, description = ?, releaseDate = ?, duration = ? " +
                    "WHERE id = ?;";
    private static final String UPDATE_MPA_RATING =
            "UPDATE filmsMpaRatings " +
                    "SET ratingId = ? " +
                    "WHERE filmId = ?;";
    private static final String DELETE_GENRES_BY_FILM_ID =
            "DELETE from filmsGenres WHERE filmId = ?;";
    private static final String GET_ALL_FILMS =
            "SELECT * from films;";
    private static final String ADD_LIKE =
            "INSERT INTO likes(userId, filmId) " +
                    "VALUES (?, ?);";
    private static final String DELETE_LIKE =
            "DELETE from likes WHERE userId = ? AND filmId = ?;";
    private static final String GET_TOP_LIKED_FILMS =
            "SELECT filmId " +
                    "FROM likes " +
                    "GROUP BY filmId " +
                    "ORDER BY COUNT(*) DESC " +
                    "LIMIT ?;";
    private static final String GET_FILMS_WITH_GENRE_ID =
            "SELECT f.* " +
                    "FROM films f " +
                    "INNER JOIN filmsGenres fg ON f.id = fg.filmId " +
                    "WHERE fg.genreId = ?;";

    public FilmDbStorage(JdbcTemplate jdbc, RowMapper<Film> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public Film addFilm(Film newFilm) {
        int filmId = insert(
                INSERT_FILM_QUERY,
                newFilm.getName(),
                newFilm.getDescription(),
                newFilm.getReleaseDate(),
                newFilm.getDuration()
        );
        newFilm.setId(filmId);
        List<Map<String, Integer>> genres = newFilm.getGenres();
        for (Map<String, Integer> genreItem : genres) {
            insertWithoutGeneratedId(INSERT_GENRE_QUERY, filmId, genreItem.get("id"));
        }
        int mpaRatingId = newFilm.getMpa().get("id");
        insertWithoutGeneratedId(INSERT_MPA_QUERY, filmId, mpaRatingId);
        return newFilm;
    }

    @Override
    public Film updateFilm(Film updatedFilm) {
        update(
                UPDATE_FILM,
                updatedFilm.getName(),
                updatedFilm.getDescription(),
                updatedFilm.getReleaseDate(),
                updatedFilm.getDuration(),
                updatedFilm.getId()
        );
        update(
                UPDATE_MPA_RATING,
                updatedFilm.getMpa().get("id"),
                updatedFilm.getId()
        );
        int rowsDeleted = jdbc.update(DELETE_GENRES_BY_FILM_ID, updatedFilm.getId());
        List<Map<String, Integer>> genres = updatedFilm.getGenres();
        for (Map<String, Integer> genreItem : genres) {
            insertWithoutGeneratedId(INSERT_GENRE_QUERY, updatedFilm.getId(), genreItem.get("id"));
        }
        return updatedFilm;
    }

    @Override
    public List<Film> getAllFilms() {
        return findMany(GET_ALL_FILMS).stream()
                .peek(film -> film.setMpa(getFilmMpaRating(film.getId())))
                .peek(film -> film.setGenres(getFilmGenres(film.getId())))
                .toList();
    }

    @Override
    public Optional<Film> getFilmById(int filmId) {
        Optional<Film> optFilm = findOne(GET_FILM_BY_ID, filmId);
        if (optFilm.isEmpty()) {
            return optFilm;
        }
        Film film = optFilm.get();
        film.setMpa(getFilmMpaRating(film.getId()));
        film.setGenres(getFilmGenres(film.getId()));
        return Optional.of(film);
    }

    @Override
    public boolean filmExists(String name, LocalDate releaseDate, int duration) {
        Integer numFilmsFound = jdbc.queryForObject(CHECK_FOR_FILM_QUERY, Integer.class, name, releaseDate, duration);
        return numFilmsFound > 0;
    }

    @Override
    public boolean mpaRatingExists(int ratingId) {
        Integer numMpaFound = jdbc.queryForObject(CHECK_FOR_MPA_QUERY, Integer.class, ratingId);
        return numMpaFound > 0;
    }

    @Override
    public boolean genreExists(int genreId) {
        Integer numGenresFound = jdbc.queryForObject(CHECK_FOR_GENRE_QUERY, Integer.class, genreId);
        return numGenresFound > 0;
    }

    @Override
    public void addLike(int userId, int filmId) {
        insertWithoutGeneratedId(ADD_LIKE, userId, filmId);
    }

    @Override
    public void removeLike(int userId, int filmId) {
        int rowsDeleted = jdbc.update(DELETE_LIKE, userId, filmId);
    }

    @Override
    public List<Integer> getTopLikedFilmIds(int count) {
        return jdbc.queryForList(GET_TOP_LIKED_FILMS, Integer.class, count);
    }

    public List<Film> getFilmsWithGenre(int genreId) {
        return findMany(GET_FILMS_WITH_GENRE_ID, genreId).stream()
                .peek(film -> film.setMpa(getFilmMpaRating(film.getId())))
                .peek(film -> film.setGenres(getFilmGenres(film.getId())))
                .toList();
    }

    private Map<String, Integer> getFilmMpaRating(int filmId) {
        int mpaRating = jdbc.queryForObject(GET_MPA_RATING_BY_FILM_ID, Integer.class, filmId);
        return Map.of("id", mpaRating);
    }

    private List<Map<String, Integer>> getFilmGenres(int filmId) {
        List<Integer> genreIds = jdbc.queryForList(GET_GENRES_BY_FILM_ID, Integer.class, filmId);
        List<Map<String, Integer>> genreItems = new ArrayList<>();
        for (int genreId : genreIds) {
            genreItems.add(Map.of("id", genreId));
        }
        return genreItems;
    }
}
