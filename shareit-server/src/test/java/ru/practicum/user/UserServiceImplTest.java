package ru.practicum.user;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.practicum.error.EmailAlreadyExistsException;
import ru.practicum.error.UserNotFoundException;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

class UserServiceImplTest {

    UserService userService;
    UserRepository userRepository = Mockito.mock(UserRepository.class);
    User user = new User(1, "user", "user@mail.ru");
    UserDto userDto = new UserDto(1, "user", "user@mail.ru");
    List<User> users = List.of(new User(1, "user", "firstuser@mail.ru"),
            new User(2, "secondUser", "secondUser@mail.ru"),
            new User(3, "thirdUser", "thirdUser@mail.ru"));

    @BeforeEach
    void beforeEach() {
        userService = new UserServiceImpl(userRepository);
    }

    @Test
    void add() {
        Mockito
                .when(userRepository.save(Mockito.any(User.class)))
                .thenReturn(user);
        UserDto foundedUserDto = userService.add(userDto);
        assertThat(foundedUserDto.getId(), equalTo(userDto.getId()));
        assertThat(foundedUserDto.getName(), equalTo(userDto.getName()));
        assertThat(foundedUserDto.getEmail(), equalTo(userDto.getEmail()));
    }

    @Test
    void update() {
        Mockito
                .when(userRepository.save(Mockito.any(User.class)))
                .thenReturn(user);
        UserDto foundedUserDto = userService.update(userDto);
        assertThat(foundedUserDto.getId(), equalTo(userDto.getId()));
        assertThat(foundedUserDto.getName(), equalTo(userDto.getName()));
        assertThat(foundedUserDto.getEmail(), equalTo(userDto.getEmail()));
    }

    @Test
    void getById() {
        Mockito
                .when(userRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.ofNullable(user));
        UserDto foundedUserDto = userService.getById(1);
        assertThat(foundedUserDto.getId(), equalTo(userDto.getId()));
        assertThat(foundedUserDto.getName(), equalTo(userDto.getName()));
        assertThat(foundedUserDto.getEmail(), equalTo(userDto.getEmail()));
    }

    @Test
    void getByIdFail() {
        Mockito
                .when(userRepository.findById(Mockito.anyInt()))
                .thenThrow(new UserNotFoundException("Пользователь с данным id не найден в базе"));
        final UserNotFoundException exception = Assertions.assertThrows(UserNotFoundException.class,
                () -> userService.getById(2));
        Assertions.assertEquals("Пользователь с данным id не найден в базе", exception.getMessage());
    }

    @Test
    void getAll() {
        Mockito
                .when(userRepository.findAll())
                .thenReturn(users);
        Collection<UserDto> userDtoList = userService.getAll();
        assertThat(users.size(), equalTo(userDtoList.size()));
        for (UserDto foundedUserDto : userDtoList) {
            if (foundedUserDto.getId().equals(1)) {
                assertThat(foundedUserDto.getId(), equalTo(1));
                assertThat(foundedUserDto.getName(), equalTo("user"));
                assertThat(foundedUserDto.getEmail(), equalTo("firstuser@mail.ru"));
            } else if (foundedUserDto.getId().equals(2)) {
                assertThat(foundedUserDto.getId(), equalTo(2));
                assertThat(foundedUserDto.getName(), equalTo("secondUser"));
                assertThat(foundedUserDto.getEmail(), equalTo("secondUser@mail.ru"));
            } else if (foundedUserDto.getId().equals(3)) {
                assertThat(foundedUserDto.getId(), equalTo(3));
                assertThat(foundedUserDto.getName(), equalTo("thirdUser"));
                assertThat(foundedUserDto.getEmail(), equalTo("thirdUser@mail.ru"));
            }
        }
    }

    @Test
    void patch() {
        Mockito
                .when(userRepository.findAll())
                .thenReturn(users);
        Mockito
                .when(userRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.ofNullable(user));
        Mockito
                .when(userRepository.save(Mockito.any(User.class)))
                .thenReturn(user);
        UserDto foundedUserDto = userService.patch(1, userDto);
        assertThat(foundedUserDto.getId(), equalTo(userDto.getId()));
        assertThat(foundedUserDto.getName(), equalTo(userDto.getName()));
        assertThat(foundedUserDto.getEmail(), equalTo(userDto.getEmail()));
    }

    @Test
    void patchFail() {
        Mockito
                .when(userRepository.findAll())
                .thenThrow(new EmailAlreadyExistsException("Пользователь с такой почтой уже есть в базе данных"));
        final EmailAlreadyExistsException exception = Assertions.assertThrows(EmailAlreadyExistsException.class,
                () -> userService.patch(1, userDto));
        Assertions.assertEquals("Пользователь с такой почтой уже есть в базе данных", exception.getMessage());
    }
}