package ru.practicum.booking;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.item.ItemDto;
import ru.practicum.user.UserDto;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class ReturnedBookingDto {

    private Integer id;

    private LocalDateTime start;

    private LocalDateTime end;

    private ItemDto item;

    private UserDto booker;

    private String status;

    public ReturnedBookingDto(Integer id, LocalDateTime start, LocalDateTime end, ItemDto item, UserDto booker, String status) {
        this.id = id;
        this.start = start;
        this.end = end;
        this.item = item;
        this.booker = booker;
        this.status = status;
    }
}
