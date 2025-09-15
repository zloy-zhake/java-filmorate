package ru.yandex.practicum.filmorate.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UpdateFilmRequest {
    private int id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private Integer duration;

    public boolean hasName() {
        return !(this.name == null || this.name.isBlank());
    }

    public boolean hasDescription() {
        return !(this.description == null || this.description.isBlank());
    }

    public boolean hasReleaseDate() {
        return !(this.releaseDate == null);
    }

    public boolean hasDuration() {
        return !(this.duration == null);
    }
}
