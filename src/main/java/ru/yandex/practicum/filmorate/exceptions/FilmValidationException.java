package ru.yandex.practicum.filmorate.exceptions;

public class FilmValidationException extends RuntimeException {
    public FilmValidationException(String msg) {
        super(msg);
    }
}
