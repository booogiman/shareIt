package ru.practicum.requests;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.booking.BookingService;
import ru.practicum.booking.ResultingBookingDto;
import ru.practicum.error.UserNotFoundException;
import ru.practicum.item.ItemDto;
import ru.practicum.item.ItemService;
import ru.practicum.user.UserDto;
import ru.practicum.user.UserService;

import java.time.LocalDateTime;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(
        properties = "ru/practicum",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class RequestServiceIntegralTest {

    private final BookingService bookingService;
    private final UserService userService;
    private final ItemService itemService;
    private final RequestService requestService;

    UserDto userDto = new UserDto(1, "user", "user@mail.ru");
    UserDto secondUserDto = new UserDto(2, "secondUser", "secondUser@mail.ru");

    ItemDto itemDto = new ItemDto(1, "Отвертка", "Хорошая отвертка", true, null,
            1, null, null);
    LocalDateTime date = LocalDateTime.of(2022, 8, 20, 13, 12);
    ResultingBookingDto resultingBookingDto = new ResultingBookingDto(
            1, date, date.plusDays(1),
            1, null, null);
    ItemRequestDto itemRequestDto = new ItemRequestDto(1, "Хотел бы поспользоваться щеткой для обуви",
            null, null, null);

    @Test
    void getById() {
        userService.add(userDto);
        userService.add(secondUserDto);
        requestService.add(2,itemRequestDto);
        itemService.add(1, itemDto);
        requestService.getById(1, 1);
        ItemRequestDto foundedItemRequestDto = requestService.getById(1, 1);
        assertThat(foundedItemRequestDto.getId(), equalTo(1));
        assertThat(foundedItemRequestDto.getDescription(), equalTo("Хотел бы поспользоваться щеткой для обуви"));
        assertThat(foundedItemRequestDto.getRequester(), equalTo(2));
        assertThat(foundedItemRequestDto.getItems().size(), equalTo(1));
    }

    @Test
    void getByIdFail() {
        RuntimeException runtimeException = assertThrows(UserNotFoundException.class, () ->
                requestService.getById(10,1));
        String message = "Пользователь с id " + 10 + " не найден";
        String actual = runtimeException.getMessage();
        assertEquals(actual, message);
    }
}
