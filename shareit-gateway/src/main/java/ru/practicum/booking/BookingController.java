package ru.practicum.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Validated
@RestController
@RequestMapping(path = "/bookings")
public class BookingController {

    private final BookingClient bookingClient;

    @Autowired
    public BookingController(BookingClient bookingClient) {
        this.bookingClient = bookingClient;
    }


    @PostMapping
    public Object add(@RequestHeader("X-Sharer-User-Id") Integer userId,
                      @RequestBody ResultingBookingDto booking) {
        return bookingClient.add(userId, booking);
    }

    @PatchMapping("/{bookingId}")
    public Object patch(@RequestHeader("X-Sharer-User-Id") Integer userId,
                        @PathVariable Integer bookingId,
                        @RequestParam(required = true) Boolean approved) {
        return bookingClient.patch(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public Object get(@RequestHeader("X-Sharer-User-Id") Integer userId,
                      @PathVariable Integer bookingId) {
        return bookingClient.getById(userId, bookingId);
    }

    @GetMapping
    public Object getAllBookingsByOwnerId(
            @RequestHeader("X-Sharer-User-Id") Integer userId,
            @RequestParam(defaultValue = "ALL", required = false) String state,
            @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
            @Positive @RequestParam(defaultValue = "10") Integer size) {
        return bookingClient.getAllBookingsByOwnerId(userId, state, from, size);
    }

    @GetMapping("/owner")
    public Object getAllBookingsForAllItemsById(
            @RequestHeader("X-Sharer-User-Id") Integer userId,
            @RequestParam(defaultValue = "ALL", required = false) String state,
            @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
            @Positive @RequestParam(defaultValue = "10") Integer size) {
        return bookingClient.getAllBookingsForAllItemsByOwnerId(userId, state, from, size);
    }
}
