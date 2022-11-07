package ru.practicum.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.error.UserNotFoundException;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(
        properties = "ru/practicum",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class UserServiceIntegralTest {

    private final UserService userService;
    UserDto userDto = new UserDto(1, "user", "user@mail.ru");

    @Test
    void getById() {
        userService.add(userDto);
        UserDto foundedUserDto = userService.getById(1);
        assertThat(foundedUserDto.getId(), equalTo(1));
        assertThat(foundedUserDto.getName(), equalTo("user"));
        assertThat(foundedUserDto.getEmail(), equalTo("user@mail.ru"));
    }

    @Test
    void getByIdFail() {
        RuntimeException runtimeException = assertThrows(UserNotFoundException.class, () ->
                userService.getById(10)
        );
        String message = "Пользователь с id " + 10 + " не найден";
        String actual = runtimeException.getMessage();
        assertEquals(actual, message);
    }
}
