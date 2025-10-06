package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.mappers.FilmRowMapper;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({FilmDbStorage.class, FilmRowMapper.class})
public class FilmDbStorageTests {

    private final FilmDbStorage filmDbStorage;

    @Test
    public void testAddFilm() {
        Film testFilm = new Film();
        testFilm.setName("Name");
        testFilm.setDescription("Description");
        testFilm.setReleaseDate(LocalDate.of(2000, 1, 1));
        testFilm.setDuration(60);
        testFilm.setGenres(new ArrayList<>());
        Film addedFilm = filmDbStorage.addFilm(testFilm);
        int idToExpect = addedFilm.getId();
        Optional<Film> filmOptional = filmDbStorage.getFilmById(idToExpect);
        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("id", idToExpect)
                );
    }

    @Test
    public void testUpdateFilm() {
        Film testFilm = new Film();
        testFilm.setName("Name");
        testFilm.setDescription("Description");
        testFilm.setReleaseDate(LocalDate.of(2000, 1, 1));
        testFilm.setDuration(60);
        testFilm.setGenres(new ArrayList<>());
        Film addedFilm = filmDbStorage.addFilm(testFilm);
        int idToExpect = addedFilm.getId();
        testFilm.setId(idToExpect);
        testFilm.setName("Updated Name");
        filmDbStorage.updateFilm(testFilm);
        Optional<Film> filmOptional = filmDbStorage.getFilmById(idToExpect);
        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("name", "Updated Name")
                );
    }

    @Test
    public void testGetAllFilms() {
        Film testFilm1 = new Film();
        testFilm1.setName("Name1");
        testFilm1.setDescription("Description1");
        testFilm1.setReleaseDate(LocalDate.of(2000, 1, 1));
        testFilm1.setDuration(60);
        testFilm1.setGenres(new ArrayList<>());
        Film addedFilm1 = filmDbStorage.addFilm(testFilm1);
        testFilm1.setId(addedFilm1.getId());
        Film testFilm2 = new Film();
        testFilm2.setName("Name2");
        testFilm2.setDescription("Description2");
        testFilm2.setReleaseDate(LocalDate.of(2001, 1, 1));
        testFilm2.setDuration(120);
        testFilm2.setGenres(new ArrayList<>());
        Film addedFilm2 = filmDbStorage.addFilm(testFilm2);
        testFilm2.setId(addedFilm2.getId());
        List<Film> films = filmDbStorage.getAllFilms();
        assertThat(films).hasSize(2)
                .contains(testFilm1)
                .contains(testFilm2);
    }

    @Test
    public void testFilmExists() {
        Film testFilm = new Film();
        testFilm.setName("Name");
        testFilm.setDescription("Description");
        testFilm.setReleaseDate(LocalDate.of(2000, 1, 1));
        testFilm.setDuration(60);
        testFilm.setGenres(new ArrayList<>());
        Film addedFilm = filmDbStorage.addFilm(testFilm);
        String nameToExpect = addedFilm.getName();
        LocalDate releaseDateToExpect = addedFilm.getReleaseDate();
        int durationToExpect = addedFilm.getDuration();

        boolean exists = filmDbStorage.filmExists(nameToExpect, releaseDateToExpect, durationToExpect);
        assertThat(exists).isTrue();
    }

    @Test
    public void testMpaRatingExists() {
        boolean exists = filmDbStorage.mpaRatingExists(1);
        assertThat(exists).isTrue();
        exists = filmDbStorage.mpaRatingExists(20);
        assertThat(exists).isFalse();
    }

    @Test
    public void testGenreExists() {
        boolean exists = filmDbStorage.genreExists(1);
        assertThat(exists).isTrue();
        exists = filmDbStorage.genreExists(20);
        assertThat(exists).isFalse();
    }
}
