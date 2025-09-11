package ru.yandex.practicum.filmorate.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import ru.yandex.practicum.filmorate.model.FriendshipStatus;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Data
public class UserDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private int id;
    private String email;
    private String login;
    private String name;
    private LocalDate birthday;
    private List<User> friends;
//    private Map<Integer, FriendshipStatus> friendshipStatuses;
}