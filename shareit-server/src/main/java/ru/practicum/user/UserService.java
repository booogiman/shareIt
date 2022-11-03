package ru.practicum.user;

import java.util.Collection;

public interface UserService {

    UserDto add(UserDto user);

    UserDto update(UserDto user);

    UserDto getById(Integer id);

    Collection<UserDto> getAll();

    void deleteById(Integer id);

    UserDto patch(Integer userId, UserDto user);
}
