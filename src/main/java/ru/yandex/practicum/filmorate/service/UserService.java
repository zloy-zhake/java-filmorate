package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.NewUserRequest;
import ru.yandex.practicum.filmorate.dto.UpdateUserRequest;
import ru.yandex.practicum.filmorate.dto.UserDto;
import ru.yandex.practicum.filmorate.exceptions.DuplicatedDataException;
import ru.yandex.practicum.filmorate.exceptions.UserValidationException;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {
    public UserStorage userStorage;

    public UserService(
            @Qualifier("userDbStorage") UserStorage userStorage
    ) {
        this.userStorage = userStorage;
    }

    public List<UserDto> getAllUsers() {
        return this.userStorage.getAllUsers().stream()
                .map(UserMapper::mapToUserDto)
                .collect(Collectors.toList());
    }

    public UserDto createUser(NewUserRequest request) {
        NewUserRequest validatedRequest = this.validateNewUserRequest(request);

        Optional<User> userAlreadyExists = userStorage.getUserByEmail(validatedRequest.getEmail());
        if (userAlreadyExists.isPresent()) {
            throw new DuplicatedDataException("Данный имейл уже используется: " + validatedRequest.getEmail());
        }
        userAlreadyExists = userStorage.getUserByLogin(validatedRequest.getLogin());
        if (userAlreadyExists.isPresent()) {
            throw new DuplicatedDataException("Данный логин уже используется: " + validatedRequest.getLogin());
        }
        User user = UserMapper.mapToUser(validatedRequest);
        user = userStorage.createUser(user);
        return UserMapper.mapToUserDto(user);
    }

    public UserDto updateUser(UpdateUserRequest request) {
        User updatedUser = userStorage.getUserById(request.getId())
                .map(user -> UserMapper.updateUserFields(user, request))
                .orElseThrow(() -> new NoSuchElementException(
                        "Пользователя с id=" + request.getId() + " нет в системе."
                ));
        updatedUser = userStorage.updateUser(updatedUser);
        return UserMapper.mapToUserDto(updatedUser);
    }

    public void addFriends(int user1Id, int user2Id) {
        if (!this.userExists(user1Id)) {
            throw new NoSuchElementException(
                    "Пользователя с id=" + user1Id + " нет в системе."
            );
        }
        if (!this.userExists(user2Id)) {
            throw new NoSuchElementException(
                    "Пользователя с id=" + user2Id + " нет в системе."
            );
        }
        this.userStorage.addFriend(user1Id, user2Id);
    }

    public List<UserDto> getUserFriends(int userId) {
        if (!this.userExists(userId)) {
            throw new NoSuchElementException(
                    "Пользователя с id=" + userId + " нет в системе."
            );
        }
        return this.userStorage.getUserFriends(userId).stream()
                .map(UserMapper::mapToUserDto)
                .toList();
    }

    public void removeFriend(int userId, int friendId) {
        if (!this.userExists(userId)) {
            throw new NoSuchElementException(
                    "Пользователя с id=" + userId + " нет в системе."
            );
        }
        if (!this.userExists(friendId)) {
            throw new NoSuchElementException(
                    "Пользователя с id=" + friendId + " нет в системе."
            );
        }
        userStorage.removeFriend(userId, friendId);
    }

    public List<UserDto> getCommonFriends(int user1Id, int user2Id) {
        Set<UserDto> user1Friends = new HashSet<>(this.getUserFriends(user1Id));
        Set<UserDto> user2Friends = new HashSet<>(this.getUserFriends(user2Id));
        user1Friends.retainAll(user2Friends);
        return new ArrayList<>(user1Friends);
    }

//    public User getUserById(int id) {
//        return this.userStorage.getUserById(id);
//    }

    // Метод возвращает объект User, потому что в процессе валидации объект может измениться
    private NewUserRequest validateNewUserRequest(NewUserRequest request) throws UserValidationException {
        if (request.getEmail() == null || request.getEmail().isBlank()) {
            log.error("Отсутствует адрес электронной почты. {}", request);
            throw new UserValidationException("Электронная почта не может быть пустой.");
        }
        if (!request.getEmail().contains("@")) {
            log.error("Адрес электронной почты не содержит \"@\". {}", request);
            throw new UserValidationException("Электронная почта должна содержать символ @.");
        }
        if (request.getLogin() == null || request.getLogin().isBlank()) {
            log.error("Отсутствует логин. {}", request);
            throw new UserValidationException("Логин не может быть пустым.");
        }
        if (request.getLogin().contains(" ")) {
            log.error("Логин содержит пробелы. {}", request);
            throw new UserValidationException("Логин не может содержать пробелы.");
        }
        // имя для отображения может быть пустым — в таком случае будет использован логин
        if (request.getName() == null || request.getName().isBlank()) {
            request.setName(request.getLogin());
            log.info("Отсутствует имя для отображения. Вместо него будет использован логин.");
        }
        if (request.getBirthday().isAfter(LocalDate.now())) {
            log.error("Указана нереалистичная дата рождения. {}", request);
            throw new UserValidationException("Дата рождения не может быть в будущем.");
        }
        return request;
    }

    private boolean userExists(int userId) {
        Optional<User> optUser = this.userStorage.getUserById(userId);
        return optUser.isPresent();
    }
}
