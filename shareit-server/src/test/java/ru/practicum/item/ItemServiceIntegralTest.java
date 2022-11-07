package ru.practicum.item;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.error.InvalidUserException;
import ru.practicum.user.UserDto;
import ru.practicum.user.UserService;

import java.time.LocalDateTime;
import java.util.Collection;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(
        properties = "ru/practicum",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ItemServiceIntegralTest {

    private final UserService userService;
    private final ItemService itemService;

    UserDto userDto = new UserDto(1, "user", "user@mail.ru");
    UserDto secondUserDto = new UserDto(2, "secondUser", "secondUser@mail.ru");
    ItemDto itemDto = new ItemDto(1, "Отвертка", "Хорошая отвертка", true, null,
            null, null, null);
    LocalDateTime date = LocalDateTime.now().plusSeconds(2);
    LocalDateTime dateFuture = LocalDateTime.of(2022, 8, 20, 13, 12);

    @Test
    void getAll() {
        userService.add(userDto);
        userService.add(secondUserDto);
        itemService.add(1, itemDto);
        Collection<ItemDto> itemDtos = itemService.getAll(1, 0, 5);
        assertThat(itemDtos.size(), equalTo(1));
        for (ItemDto foundedItemDto : itemDtos) {
            if (foundedItemDto.getId().equals(1)) {
                assertThat(foundedItemDto.getId(), equalTo(1));
                assertThat(foundedItemDto.getName(), equalTo("Отвертка"));
                assertThat(foundedItemDto.getDescription(), equalTo("Хорошая отвертка"));
                assertThat(foundedItemDto.getOwnerId(), equalTo(1));
            }
        }
    }

    @Test
    void getAllFail() {
        RuntimeException runtimeException = assertThrows(InvalidUserException.class, () ->
                itemService.getAll(10, 0, 5));
        String message = "Пользователь не добавлен в систему";
        String actual = runtimeException.getMessage();
        assertEquals(actual, message);
    }

}
