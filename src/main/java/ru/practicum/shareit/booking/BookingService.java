package ru.practicum.shareit.booking;

import java.util.List;

public interface BookingService {

    ReturnedBookingDto add(Integer userId, ResultingBookingDto resultingBookingDto);

    ReturnedBookingDto patch(Integer userId, Integer bookingId, Boolean approved);

    ReturnedBookingDto getById(Integer userId, Integer bookingId);

    List<ReturnedBookingDto> getAllBookingsByOwnerId(Integer userId, String state);

    List<ReturnedBookingDto> getAllBookingsForAllItemsByOwnerId(Integer userId, String state);

}
