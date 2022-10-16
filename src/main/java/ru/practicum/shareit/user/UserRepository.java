package ru.practicum.shareit.user;

import java.util.HashMap;

public interface UserRepository {

    UserDto add(User user);

    UserDto update(User user);

    UserDto getById(Integer id);

    HashMap<Integer, UserDto> getAll();

    UserDto patch(Integer userId, User user);

    Boolean deleteById(Integer id);
}
