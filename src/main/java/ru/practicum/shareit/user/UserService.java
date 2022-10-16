package ru.practicum.shareit.user;

import java.util.Collection;

public interface UserService {

    UserDto add(UserDto user);

    UserDto update(UserDto user);

    UserDto getById(Integer id);

    Collection<UserDto> getAll();

    Boolean deleteById(Integer id);

    UserDto patch(Integer userId, UserDto user);
}
