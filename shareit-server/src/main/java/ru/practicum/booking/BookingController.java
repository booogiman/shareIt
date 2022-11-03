package ru.practicum.booking;

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
    public ReturnedBookingDto add(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                  @RequestBody ResultingBookingDto booking) {
        return bookingService.add(userId, booking);
    }

    @PatchMapping("/{bookingId}")
    public ReturnedBookingDto patch(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                    @PathVariable Integer bookingId,
                                    @RequestParam(required = true) Boolean approved) {
        return bookingService.patch(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public ReturnedBookingDto get(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                  @PathVariable Integer bookingId) {
        return bookingService.getById(userId, bookingId);
    }

    @GetMapping
    public Collection<ReturnedBookingDto> getAllBookingsByOwnerId(
            @RequestHeader("X-Sharer-User-Id") Integer userId,
            @RequestParam(defaultValue = "ALL", required = false) String state,
            @RequestParam(defaultValue = "0", required = false) Integer from,
            @RequestParam(defaultValue = "10", required = false) Integer size) {
        return bookingService.getAllBookingsByOwnerId(userId, state, from, size);
    }

    @GetMapping("/owner")
    public Collection<ReturnedBookingDto> getAllBookingsForAllItemsById(
            @RequestHeader("X-Sharer-User-Id") Integer userId,
            @RequestParam(defaultValue = "ALL", required = false) String state,
            @RequestParam(defaultValue = "0", required = false) Integer from,
            @RequestParam(defaultValue = "10", required = false) Integer size) {
        return bookingService.getAllBookingsForAllItemsByOwnerId(userId, state, from, size);
    }
}
