package ru.practicum.booking;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.error.BookingNotFoundException;
import ru.practicum.error.InvalidAccessException;
import ru.practicum.item.ItemDto;
import ru.practicum.item.ItemService;
import ru.practicum.user.UserDto;
import ru.practicum.user.UserService;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;

@SpringBootTest(
        properties = "ru/practicum",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class BookingServiceIntegralTest {

    private final BookingService bookingService;
    private final UserService userService;
    private final ItemService itemService;
    UserDto userDto = new UserDto(1, "user", "user@mail.ru");
    UserDto secondUserDto = new UserDto(2, "secondUser", "secondUser@mail.ru");

    ItemDto itemDto = new ItemDto(1, "Отвертка", "Хорошая отвертка", true, null,
            null, null, null);
    LocalDateTime date = LocalDateTime.of(2022, 8, 20, 13, 12);
    ResultingBookingDto resultingBookingDto = new ResultingBookingDto(
            1, date, date.plusDays(1),
            1, null, null);

    @Test
    void getById() {
        userService.add(userDto);
        userService.add(secondUserDto);
        itemService.add(1, itemDto);
        bookingService.add(2, resultingBookingDto);
        ReturnedBookingDto returnedBookingDto = bookingService.getById(2, 1);
        assertThat(returnedBookingDto.getId(), equalTo(1));
        assertThat(returnedBookingDto.getStart(), equalTo(date));
        assertThat(returnedBookingDto.getEnd(), equalTo(date.plusDays(1)));
        assertThat(returnedBookingDto.getItem().getId(), equalTo(1));
        assertThat(returnedBookingDto.getItem().getName(), equalTo("Отвертка"));
        assertThat(returnedBookingDto.getBooker().getId(), equalTo(2));
        assertThat(returnedBookingDto.getBooker().getName(), equalTo("secondUser"));
        assertThat(returnedBookingDto.getStatus(), equalTo(Status.WAITING.toString()));
    }

    @Test
    void getByIdFailBookingNotFound() {
        RuntimeException runtimeException = assertThrows(BookingNotFoundException.class, () ->
                bookingService.getById(2, 10)
        );
        String message = "Бронирование с id " + 10 + " не найдено";
        String actual = runtimeException.getMessage();
        assertEquals(actual, message);
    }

    @Test
    void addFail() {
        userService.add(userDto);
        userService.add(secondUserDto);
        itemService.add(1, itemDto);
        bookingService.add(2, resultingBookingDto);
        RuntimeException runtimeException = assertThrows(InvalidAccessException.class, () ->
                bookingService.getById(3, 1)
        );
        String message = "Получить данные о конкретном бронировании может либо автор бронирования, либо владелей вещи";
        String actual = runtimeException.getMessage();
        assertEquals(actual, message);
    }
}
