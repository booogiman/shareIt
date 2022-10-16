package ru.practicum.shareit.user;

import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class InMemoryUserRepository implements UserRepository {

    private final HashMap<Integer, User> users = new HashMap<>();
    private Integer idMax = 0;

    private Integer getIdMax() {
        idMax = idMax + 1;
        return idMax;
    }

    @Override
    public UserDto add(User user) {
        user.setId(getIdMax());
        users.put(user.getId(), user);
        return UserMapper.toUserDto(users.get(user.getId()));
    }

    @Override
    public UserDto update(User user) {
        users.put(user.getId(), user);
        return UserMapper.toUserDto(users.get(user.getId()));
    }

    @Override
    public UserDto getById(Integer id) {
        return UserMapper.toUserDto(users.get(id));
    }

    @Override
    public HashMap<Integer, UserDto> getAll() {
        return UserMapper.toUserDtoMap(users);
    }

    @Override
    public UserDto patch(Integer userId, User user) {
        User foundedUser = users.get(userId);
        if (user.getName() != null) {
            foundedUser.setName(user.getName());
        }
        if (user.getEmail() != null) {
            foundedUser.setEmail(user.getEmail());
        }
        return UserMapper.toUserDto(foundedUser);
    }

    @Override
    public Boolean deleteById(Integer id) {
        users.remove(id);
        return true;
    }
}
