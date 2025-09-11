package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.NewUserRequest;
import ru.yandex.practicum.filmorate.dto.UpdateUserRequest;
import ru.yandex.practicum.filmorate.dto.UserDto;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final UserService userService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<UserDto> getAllUsers() {
        return this.userService.getAllUsers();
    }

    @GetMapping("/{id}/friends")
    @ResponseStatus(HttpStatus.OK)
    public List<Integer> getUserFriends(@PathVariable int id) {
        return this.userService.getUserFriends(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto createUser(@RequestBody NewUserRequest newUserRequest) {
        return this.userService.createUser(newUserRequest);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public UserDto updateUser(@RequestBody UpdateUserRequest request) {
        return this.userService.updateUser(request);
    }

    @PutMapping("/{id}/friends/{friendId}")
    @ResponseStatus(HttpStatus.OK)
    public void addFriends(@PathVariable int id, @PathVariable int friendId) {
        this.userService.addFriends(id, friendId);
    }

//    @GetMapping("/{id}")
//    @ResponseStatus(HttpStatus.OK)
//    public User getUserById(@PathVariable int id) {
//        return this.userService.getUserById(id);
//    }
//
//
//    @GetMapping("/{id}/friends/common/{otherId}")
//    @ResponseStatus(HttpStatus.OK)
//    public List<User> getCommonFriends(@PathVariable int id, @PathVariable int otherId) {
//        return this.userService.listCommonFriends(id, otherId);
//    }
//
//    @DeleteMapping("/{id}/friends/{friendId}")
//    @ResponseStatus(HttpStatus.OK)
//    public void removeFriends(@PathVariable int id, @PathVariable int friendId) {
//        this.userService.removeFriends(id, friendId);
//    }
}
