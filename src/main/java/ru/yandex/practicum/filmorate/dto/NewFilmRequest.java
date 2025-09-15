package ru.yandex.practicum.filmorate.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class NewFilmRequest {
    private String name;
    private String description;
    private LocalDate releaseDate;
    private int duration;
}
