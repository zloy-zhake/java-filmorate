package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.UserValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;
import java.util.Set;

@Service
public class UserService {
    public UserStorage userStorage;

    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public void addFriends(int user1Id, int user2Id) {
        // Пока пользователям не надо одобрять заявки в друзья — добавляем сразу.
        // То есть если Лена стала другом Саши, то это значит, что Саша теперь друг Лены.
        User user1 = userStorage.getUserById(user1Id);
        User user2 = userStorage.getUserById(user2Id);
        user1.addFriend(user2);
        user2.addFriend(user1);
    }

    public void removeFriend(User user1, User user2) {
        if (user1 == null || user2 == null) {
            throw new UserValidationException("Пользователь не может быть null.");
        }
        user1.removeFriend(user2);
        user2.removeFriend(user1);
    }

    public List<User> listCommonFriends(User user1, User user2) {
        Set<User> friendsOfUser1 = user1.getFriends();
        Set<User> friendsOfUser2 = user2.getFriends();
        friendsOfUser1.retainAll(friendsOfUser2);
        return friendsOfUser1.stream().toList();
    }
}
