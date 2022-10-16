package ru.practicum.shareit.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping(path = "/bookings")
public class BookingController {

    private final BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public BookingDto add(@RequestBody BookingDto booking) {
        return bookingService.add(booking);
    }

    @PutMapping
    public BookingDto update(@RequestBody BookingDto booking) {
        return bookingService.update(booking);
    }

    @GetMapping("/{id}")
    public BookingDto get(@PathVariable Integer id) {
        return bookingService.getById(id);
    }

    @GetMapping
    public Collection<BookingDto> getAll() {
        return bookingService.getAll();
    }

    @DeleteMapping("/{id}")
    public Boolean delete(@PathVariable Integer id) {
        return bookingService.deleteById(id);
    }
}
