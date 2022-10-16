package ru.practicum.shareit.booking;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ItemBookingDto {

    private Integer id;

    private Integer bookerId;

    public ItemBookingDto(Integer id, Integer bookerId) {
        this.id = id;
        this.bookerId = bookerId;
    }
}
