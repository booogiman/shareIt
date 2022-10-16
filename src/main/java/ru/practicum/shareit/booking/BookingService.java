package ru.practicum.shareit.booking;

import java.util.Collection;

public interface BookingService {

    BookingDto add(BookingDto booking);

    BookingDto update(BookingDto booking);

    BookingDto getById(Integer id);

    Collection<BookingDto> getAll();

    Boolean deleteById(Integer id);

}
