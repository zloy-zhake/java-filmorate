package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
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

//    @GetMapping("/{id}")
//    @ResponseStatus(HttpStatus.OK)
//    public User getUserById(@PathVariable int id) {
//        return this.userService.getUserById(id);
//    }
//
//    @GetMapping("/{id}/friends")
//    @ResponseStatus(HttpStatus.OK)
//    public List<User> getUserFriends(@PathVariable int id) {
//        return this.userService.getUserFriends(id);
//    }
//
//    @GetMapping("/{id}/friends/common/{otherId}")
//    @ResponseStatus(HttpStatus.OK)
//    public List<User> getCommonFriends(@PathVariable int id, @PathVariable int otherId) {
//        return this.userService.listCommonFriends(id, otherId);
//    }
//
//    @PostMapping
//    @ResponseStatus(HttpStatus.CREATED)
//    public User createUser(@RequestBody User newUser) {
//        return this.userService.createUser(newUser);
//    }
//
//    @PutMapping
//    @ResponseStatus(HttpStatus.OK)
//    public User updateUser(@RequestBody User updatedUser) {
//        return this.userService.updateUser(updatedUser);
//    }
//
//    @PutMapping("/{id}/friends/{friendId}")
//    @ResponseStatus(HttpStatus.OK)
//    public void addFriends(@PathVariable int id, @PathVariable int friendId) {
//        this.userService.addFriends(id, friendId);
//    }
//
//    @DeleteMapping("/{id}/friends/{friendId}")
//    @ResponseStatus(HttpStatus.OK)
//    public void removeFriends(@PathVariable int id, @PathVariable int friendId) {
//        this.userService.removeFriends(id, friendId);
//    }
}
