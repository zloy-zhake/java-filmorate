package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exceptions.FilmValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FilmValidationTests {
    FilmController fc;
    Film testFilm;

    @BeforeEach
    void setUp() {
        fc = new FilmController();
        testFilm = new Film();
        testFilm.setId(1);
        testFilm.setName("Name");
        testFilm.setDescription("Description");
        testFilm.setReleaseDate(LocalDate.of(2001, 1, 1));
        testFilm.setDuration(50);
    }

    @Test
    void testValidFilm() {
        assertDoesNotThrow(() -> fc.addFilm(testFilm));
    }

    @Test
    void testNoName() {
        testFilm.setName("");
        assertThrows(FilmValidationException.class, () -> fc.addFilm(testFilm));
    }

    @Test
    void testLongDescription() {
        testFilm.setDescription("a".repeat(201));
        assertThrows(FilmValidationException.class, () -> fc.addFilm(testFilm));
    }

    @Test
    void testEarlyReleaseDate() {
        testFilm.setReleaseDate(LocalDate.of(1894, 12, 28));
        assertThrows(FilmValidationException.class, () -> fc.addFilm(testFilm));
    }

    @Test
    void testNegativeDuration() {
        testFilm.setDuration(-1);
        assertThrows(FilmValidationException.class, () -> fc.addFilm(testFilm));
    }
}
