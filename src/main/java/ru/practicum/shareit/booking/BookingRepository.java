package ru.practicum.shareit.booking;

import java.util.HashMap;

public interface BookingRepository {

    BookingDto add(Booking booking);

    BookingDto update(Booking booking);

    BookingDto getById(Integer id);

    HashMap<Integer, BookingDto> getAll();

    Boolean deleteById(Integer id);
}
