package ru.practicum.shareit.booking;

import java.util.Collection;

public interface BookingService {

    ReturnedBookingDto add(Integer userId, ResultingBookingDto resultingBookingDto);

    ReturnedBookingDto patch(Integer userId, Integer bookingId, Boolean approved);

    ReturnedBookingDto getById(Integer userId, Integer bookingId);

    Collection<ReturnedBookingDto> getAllBookingsByOwnerId(Integer userId, String state, Integer from, Integer page);

    Collection<ReturnedBookingDto> getAllBookingsForAllItemsByOwnerId(Integer userId, String state, Integer from, Integer page);

}
