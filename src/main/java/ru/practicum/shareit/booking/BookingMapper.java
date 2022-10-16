package ru.practicum.shareit.booking;

import java.util.HashMap;

public class BookingMapper {

    public static BookingDto toBookingDto(Booking booking) {
        return new BookingDto(booking.getId(), booking.getStart(), booking.getEnd(),
                booking.getItem() != null ? booking.getItem().getId() : null,
                booking.getBooker() != null ? booking.getBooker().getId() : null,
                booking.getStatus() != null ? booking.getStatus().toString() : null);
    }

    public static HashMap<Integer, BookingDto> toBookingDtoMap(HashMap<Integer, Booking> bookings) {
        HashMap<Integer, BookingDto> bookingDtoMap = new HashMap<>();
        for (Booking booking : bookings.values()) {
            bookingDtoMap.put(booking.getId(), toBookingDto(booking));
        }
        return bookingDtoMap;
    }

    public static Booking toBooking(BookingDto bookingDto) {
        return new Booking(bookingDto.getId(), bookingDto.getStart(), bookingDto.getEnd(),
                null, null, null);
    }

}
