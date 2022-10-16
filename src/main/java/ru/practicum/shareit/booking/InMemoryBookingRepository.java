package ru.practicum.shareit.booking;

import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class InMemoryBookingRepository implements BookingRepository {

    private final HashMap<Integer, Booking> bookings = new HashMap<>();

    private Integer idMax = 0;

    private Integer getIdMax() {
        idMax = idMax + 1;
        return idMax;
    }

    @Override
    public BookingDto add(Booking booking) {
        booking.setId(getIdMax());
        bookings.put(booking.getId(), booking);
        return BookingMapper.toBookingDto(booking);
    }

    @Override
    public BookingDto update(Booking booking) {
        bookings.put(booking.getId(), booking);
        return BookingMapper.toBookingDto(booking);
    }

    @Override
    public BookingDto getById(Integer id) {
       return BookingMapper.toBookingDto(bookings.get(id));
    }

    @Override
    public HashMap<Integer, BookingDto> getAll() {
        return BookingMapper.toBookingDtoMap(bookings);
    }

    @Override
    public Boolean deleteById(Integer id) {
        bookings.remove(id);
        return true;
    }
}
