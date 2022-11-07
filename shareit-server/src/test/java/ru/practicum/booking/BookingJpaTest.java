package ru.practicum.booking;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.item.Item;
import ru.practicum.item.ItemRepository;
import ru.practicum.user.User;
import ru.practicum.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class BookingJpaTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    BookingRepository bookingRepository;

    @Test
    void findAllUsersBookings() {
        User user = new User(1, "user", "user@mail.ru");
        User secondUser = new User(2, "secondUser", "secondUser@mail.ru");
        userRepository.save(user);
        userRepository.save(secondUser);

        Item item = new Item(1, "Отвертка", "Хорошая отвертка", true, 1, null);
        itemRepository.save(item);

        LocalDateTime firstDateStart = LocalDateTime.now().plusDays(1);
        LocalDateTime firstDateEnd = LocalDateTime.now().plusDays(2);
        LocalDateTime secondDateStart = LocalDateTime.now().plusDays(3);
        LocalDateTime secondDateEnd = LocalDateTime.now().plusDays(4);

        Booking firstBooking = new Booking(1, firstDateStart, firstDateEnd, item, secondUser, Status.WAITING.toString());
        Booking secondBooking = new Booking(2, secondDateStart, secondDateEnd, item, secondUser, Status.WAITING.toString());
        bookingRepository.save(firstBooking);
        bookingRepository.save(secondBooking);

        Page<Booking> bookingPage = bookingRepository.findAllUsersBookings(1, PageRequest.of(0, 5));
        assertThat(bookingPage.toList().size(), equalTo(2));
        for (Booking booking : bookingPage) {
            if (booking.getId().equals(1)) {
                assertThat(booking.getId(), equalTo(1));
                assertThat(booking.getStart(), equalTo(firstDateStart));
                assertThat(booking.getEnd(), equalTo(firstDateEnd));
                assertThat(booking.getItem().getId(), equalTo(1));
                assertThat(booking.getItem().getName(), equalTo("Отвертка"));
                assertThat(booking.getBooker().getId(), equalTo(2));
                assertThat(booking.getBooker().getName(), equalTo("secondUser"));
                assertThat(booking.getStatus(), equalTo(Status.WAITING.toString()));
            } else if (booking.getId().equals(2)) {
                assertThat(booking.getId(), equalTo(2));
                assertThat(booking.getStart(), equalTo(secondDateStart));
                assertThat(booking.getEnd(), equalTo(secondDateEnd));
                assertThat(booking.getItem().getId(), equalTo(1));
                assertThat(booking.getItem().getName(), equalTo("Отвертка"));
                assertThat(booking.getBooker().getId(), equalTo(2));
                assertThat(booking.getBooker().getName(), equalTo("secondUser"));
                assertThat(booking.getStatus(), equalTo(Status.WAITING.toString()));
            }
        }
    }

    @Test
    void findAllCurrentUsersBookings() {
        User user = new User(1, "user", "user@mail.ru");
        User secondUser = new User(2, "secondUser", "secondUser@mail.ru");
        userRepository.save(user);
        userRepository.save(secondUser);

        Item item = new Item(1, "Отвертка", "Хорошая отвертка",
                false, 1, null);
        Item secondItem = new Item(2, "Паяльник", "Надежный паяльник",
                false, 1, null);
        itemRepository.save(item);
        itemRepository.save(secondItem);

        LocalDateTime dateStartFirstBooking = LocalDateTime.now().minusHours(3);
        LocalDateTime dateEndFirstBooking = LocalDateTime.now().plusDays(1);
        LocalDateTime dateStartSecondBooking = LocalDateTime.now().minusHours(3);
        LocalDateTime dateEndSecondBooking = LocalDateTime.now().plusDays(1);
        LocalDateTime dateStartFutureBooking = LocalDateTime.now().plusDays(3);
        LocalDateTime dateEndFutureBooking = LocalDateTime.now().plusDays(4);

        Booking firstBooking = new Booking(1, dateStartFirstBooking, dateEndFirstBooking,
                item, secondUser, Status.APPROVED.toString());
        Booking secondBooking = new Booking(2, dateStartSecondBooking, dateEndSecondBooking,
                secondItem, secondUser, Status.APPROVED.toString());
        Booking thirdBooking = new Booking(3, dateStartFutureBooking, dateEndFutureBooking,
                item, secondUser, Status.APPROVED.toString());
        bookingRepository.save(firstBooking);
        bookingRepository.save(secondBooking);
        bookingRepository.save(thirdBooking);

        Page<Booking> bookingPage = bookingRepository.findAllCurrentUsersBookings(1, LocalDateTime.now(),
                PageRequest.of(0, 5));
        assertThat(bookingPage.toList().size(), equalTo(2));
        for (Booking booking : bookingPage) {
            if (booking.getId().equals(1)) {
                assertThat(booking.getId(), equalTo(1));
                assertThat(booking.getStart(), equalTo(dateStartFirstBooking));
                assertThat(booking.getEnd(), equalTo(dateEndFirstBooking));
                assertThat(booking.getItem().getId(), equalTo(1));
                assertThat(booking.getItem().getName(), equalTo("Отвертка"));
                assertThat(booking.getBooker().getId(), equalTo(2));
                assertThat(booking.getBooker().getName(), equalTo("secondUser"));
                assertThat(booking.getStatus(), equalTo(Status.APPROVED.toString()));
            } else if (booking.getId().equals(2)) {
                assertThat(booking.getId(), equalTo(2));
                assertThat(booking.getStart(), equalTo(dateStartSecondBooking));
                assertThat(booking.getEnd(), equalTo(dateEndSecondBooking));
                assertThat(booking.getItem().getId(), equalTo(2));
                assertThat(booking.getItem().getName(), equalTo("Паяльник"));
                assertThat(booking.getBooker().getId(), equalTo(2));
                assertThat(booking.getBooker().getName(), equalTo("secondUser"));
                assertThat(booking.getStatus(), equalTo(Status.APPROVED.toString()));
            }
        }
    }

    @Test
    void findAllPastUsersBookings() {
        User user = new User(1, "user", "user@mail.ru");
        User secondUser = new User(2, "secondUser", "secondUser@mail.ru");
        userRepository.save(user);
        userRepository.save(secondUser);

        Item item = new Item(1, "Отвертка", "Хорошая отвертка",
                false, 1, null);
        Item secondItem = new Item(2, "Паяльник", "Надежный паяльник",
                false, 1, null);
        itemRepository.save(item);
        itemRepository.save(secondItem);

        LocalDateTime dateStartFirstBooking = LocalDateTime.now().minusDays(4);
        LocalDateTime dateEndFirstBooking = LocalDateTime.now().minusDays(3);
        LocalDateTime dateStartSecondBooking = LocalDateTime.now().minusDays(2);
        LocalDateTime dateEndSecondBooking = LocalDateTime.now().minusDays(1);
        LocalDateTime dateStartFutureBooking = LocalDateTime.now().plusDays(1);
        LocalDateTime dateEndFutureBooking = LocalDateTime.now().plusDays(2);

        Booking firstBooking = new Booking(1, dateStartFirstBooking, dateEndFirstBooking,
                item, secondUser, Status.APPROVED.toString());
        Booking secondBooking = new Booking(2, dateStartSecondBooking, dateEndSecondBooking,
                secondItem, secondUser, Status.APPROVED.toString());
        Booking thirdBooking = new Booking(3, dateStartFutureBooking, dateEndFutureBooking,
                item, secondUser, Status.APPROVED.toString());
        bookingRepository.save(firstBooking);
        bookingRepository.save(secondBooking);
        bookingRepository.save(thirdBooking);

        Page<Booking> bookingPage = bookingRepository.findAllPastUsersBookings(1, LocalDateTime.now(),
                LocalDateTime.now(), PageRequest.of(0, 5));
        assertThat(bookingPage.toList().size(), equalTo(2));
        for (Booking booking : bookingPage) {
            if (booking.getId().equals(1)) {
                assertThat(booking.getId(), equalTo(1));
                assertThat(booking.getStart(), equalTo(dateStartFirstBooking));
                assertThat(booking.getEnd(), equalTo(dateEndFirstBooking));
                assertThat(booking.getItem().getId(), equalTo(1));
                assertThat(booking.getItem().getName(), equalTo("Отвертка"));
                assertThat(booking.getBooker().getId(), equalTo(2));
                assertThat(booking.getBooker().getName(), equalTo("secondUser"));
                assertThat(booking.getStatus(), equalTo(Status.APPROVED.toString()));
            } else if (booking.getId().equals(2)) {
                assertThat(booking.getId(), equalTo(2));
                assertThat(booking.getStart(), equalTo(dateStartSecondBooking));
                assertThat(booking.getEnd(), equalTo(dateEndSecondBooking));
                assertThat(booking.getItem().getId(), equalTo(2));
                assertThat(booking.getItem().getName(), equalTo("Паяльник"));
                assertThat(booking.getBooker().getId(), equalTo(2));
                assertThat(booking.getBooker().getName(), equalTo("secondUser"));
                assertThat(booking.getStatus(), equalTo(Status.APPROVED.toString()));
            }
        }
    }

    @Test
    void finnAllFutureUsersBookings() {
        User user = new User(1, "user", "user@mail.ru");
        User secondUser = new User(2, "secondUser", "secondUser@mail.ru");
        userRepository.save(user);
        userRepository.save(secondUser);

        Item item = new Item(1, "Отвертка", "Хорошая отвертка", true, 1, null);
        itemRepository.save(item);

        LocalDateTime firstDateStart = LocalDateTime.now().plusDays(1);
        LocalDateTime firstDateEnd = LocalDateTime.now().plusDays(2);
        LocalDateTime secondDateStart = LocalDateTime.now().plusDays(3);
        LocalDateTime secondDateEnd = LocalDateTime.now().plusDays(4);
        LocalDateTime dateStartPastBooking = LocalDateTime.now().minusDays(3);
        LocalDateTime dateEndPastBooking = LocalDateTime.now().minusDays(2);

        Booking firstBooking = new Booking(1, firstDateStart, firstDateEnd, item,
                secondUser, Status.APPROVED.toString());
        Booking secondBooking = new Booking(2, secondDateStart, secondDateEnd, item,
                secondUser, Status.APPROVED.toString());
        Booking thirdBooking = new Booking(3, dateStartPastBooking, dateEndPastBooking, item,
                secondUser, Status.APPROVED.toString());
        bookingRepository.save(firstBooking);
        bookingRepository.save(secondBooking);
        bookingRepository.save(thirdBooking);

        Page<Booking> bookingPage = bookingRepository.finnAllFutureUsersBookings(1, LocalDateTime.now(),
                LocalDateTime.now(), PageRequest.of(0, 5));
        assertThat(bookingPage.toList().size(), equalTo(2));
        for (Booking booking : bookingPage) {
            if (booking.getId().equals(1)) {
                assertThat(booking.getId(), equalTo(1));
                assertThat(booking.getStart(), equalTo(firstDateStart));
                assertThat(booking.getEnd(), equalTo(firstDateEnd));
                assertThat(booking.getItem().getId(), equalTo(1));
                assertThat(booking.getItem().getName(), equalTo("Отвертка"));
                assertThat(booking.getBooker().getId(), equalTo(2));
                assertThat(booking.getBooker().getName(), equalTo("secondUser"));
                assertThat(booking.getStatus(), equalTo(Status.APPROVED.toString()));
            } else if (booking.getId().equals(2)) {
                assertThat(booking.getId(), equalTo(2));
                assertThat(booking.getStart(), equalTo(secondDateStart));
                assertThat(booking.getEnd(), equalTo(secondDateEnd));
                assertThat(booking.getItem().getId(), equalTo(1));
                assertThat(booking.getItem().getName(), equalTo("Отвертка"));
                assertThat(booking.getBooker().getId(), equalTo(2));
                assertThat(booking.getBooker().getName(), equalTo("secondUser"));
                assertThat(booking.getStatus(), equalTo(Status.APPROVED.toString()));
            }
        }
    }

    @Test
    void findAllUsersBookingsWithStatus() {
        User user = new User(1, "user", "user@mail.ru");
        User secondUser = new User(2, "secondUser", "secondUser@mail.ru");
        userRepository.save(user);
        userRepository.save(secondUser);

        Item item = new Item(1, "Отвертка", "Хорошая отвертка", true, 1, null);
        itemRepository.save(item);

        LocalDateTime firstDateStart = LocalDateTime.now().plusDays(1);
        LocalDateTime firstDateEnd = LocalDateTime.now().plusDays(2);
        LocalDateTime secondDateStart = LocalDateTime.now().plusDays(3);
        LocalDateTime secondDateEnd = LocalDateTime.now().plusDays(4);
        LocalDateTime dateStartPastBooking = LocalDateTime.now().minusDays(3);
        LocalDateTime dateEndPastBooking = LocalDateTime.now().minusDays(2);

        Booking firstBooking = new Booking(1, firstDateStart, firstDateEnd, item,
                secondUser, Status.REJECTED.toString());
        Booking secondBooking = new Booking(2, secondDateStart, secondDateEnd, item,
                secondUser, Status.APPROVED.toString());
        Booking thirdBooking = new Booking(3, dateStartPastBooking, dateEndPastBooking, item,
                secondUser, Status.APPROVED.toString());
        bookingRepository.save(firstBooking);
        bookingRepository.save(secondBooking);
        bookingRepository.save(thirdBooking);

        Page<Booking> bookingPage = bookingRepository.findAllUsersBookingsWithStatus(1, "APPROVED",
                PageRequest.of(0, 5));
        assertThat(bookingPage.toList().size(), equalTo(2));
        for (Booking booking : bookingPage) {
            if (booking.getId().equals(2)) {
                assertThat(booking.getId(), equalTo(2));
                assertThat(booking.getStart(), equalTo(secondDateStart));
                assertThat(booking.getEnd(), equalTo(secondDateEnd));
                assertThat(booking.getItem().getId(), equalTo(1));
                assertThat(booking.getItem().getName(), equalTo("Отвертка"));
                assertThat(booking.getBooker().getId(), equalTo(2));
                assertThat(booking.getBooker().getName(), equalTo("secondUser"));
                assertThat(booking.getStatus(), equalTo(Status.APPROVED.toString()));
            } else if (booking.getId().equals(3)) {
                assertThat(booking.getId(), equalTo(3));
                assertThat(booking.getStart(), equalTo(dateStartPastBooking));
                assertThat(booking.getEnd(), equalTo(dateEndPastBooking));
                assertThat(booking.getItem().getId(), equalTo(1));
                assertThat(booking.getItem().getName(), equalTo("Отвертка"));
                assertThat(booking.getBooker().getId(), equalTo(2));
                assertThat(booking.getBooker().getName(), equalTo("secondUser"));
                assertThat(booking.getStatus(), equalTo(Status.APPROVED.toString()));
            }
        }
    }

    @Test
    void findLastBooking() {
        User user = new User(1, "user", "user@mail.ru");
        User secondUser = new User(2, "secondUser", "secondUser@mail.ru");
        userRepository.save(user);
        userRepository.save(secondUser);

        Item item = new Item(1, "Отвертка", "Хорошая отвертка", true,
                1, null);
        itemRepository.save(item);

        LocalDateTime firstDateStart = LocalDateTime.now().plusDays(2);
        LocalDateTime firstDateEnd = LocalDateTime.now().plusDays(3);
        LocalDateTime secondDateStart = LocalDateTime.now().plusDays(4);
        LocalDateTime secondDateEnd = LocalDateTime.now().plusDays(5);
        LocalDateTime thirdDateStart = LocalDateTime.now().minusHours(1);
        LocalDateTime thirdDateEnd = LocalDateTime.now().plusDays(1);
        LocalDateTime forthDateStart = LocalDateTime.now().minusDays(4);
        LocalDateTime forthDateEnd = LocalDateTime.now().minusDays(3);
        LocalDateTime fifthDateStart = LocalDateTime.now().minusDays(2);
        LocalDateTime fifthDateEnd = LocalDateTime.now().minusDays(1);

        Booking firstBooking = new Booking(1, forthDateStart, forthDateEnd, item,
                secondUser, Status.APPROVED.toString());
        Booking secondBooking = new Booking(2, fifthDateStart, fifthDateEnd, item,
                secondUser, Status.APPROVED.toString());
        Booking thirdBooking = new Booking(3, thirdDateStart, thirdDateEnd, item,
                secondUser, Status.APPROVED.toString());
        Booking forthBooking = new Booking(4, firstDateStart, firstDateEnd, item,
                secondUser, Status.APPROVED.toString());
        Booking fifthBooking = new Booking(5, secondDateStart, secondDateEnd, item,
                secondUser, Status.APPROVED.toString());
        bookingRepository.save(firstBooking);
        bookingRepository.save(secondBooking);
        bookingRepository.save(thirdBooking);
        bookingRepository.save(forthBooking);
        bookingRepository.save(fifthBooking);

        Booking lastBooking = bookingRepository.findLastBooking(1, 1);
        assertThat(lastBooking.getId(), equalTo(2));
        assertThat(lastBooking.getStart(), equalTo(fifthDateStart));
        assertThat(lastBooking.getEnd(), equalTo(fifthDateEnd));
        assertThat(lastBooking.getItem().getId(), equalTo(1));
        assertThat(lastBooking.getItem().getName(), equalTo("Отвертка"));
        assertThat(lastBooking.getBooker().getId(), equalTo(2));
        assertThat(lastBooking.getBooker().getName(), equalTo("secondUser"));
        assertThat(lastBooking.getStatus(), equalTo(Status.APPROVED.toString()));
    }

    @Test
    void findNextBooking() {
        User user = new User(1, "user", "user@mail.ru");
        User secondUser = new User(2, "secondUser", "secondUser@mail.ru");
        userRepository.save(user);
        userRepository.save(secondUser);

        Item item = new Item(1, "Отвертка", "Хорошая отвертка", true,
                1, null);
        itemRepository.save(item);

        LocalDateTime firstDateStart = LocalDateTime.now().plusDays(2);
        LocalDateTime firstDateEnd = LocalDateTime.now().plusDays(3);
        LocalDateTime secondDateStart = LocalDateTime.now().plusDays(4);
        LocalDateTime secondDateEnd = LocalDateTime.now().plusDays(5);
        LocalDateTime thirdDateStart = LocalDateTime.now().minusHours(1);
        LocalDateTime thirdDateEnd = LocalDateTime.now().plusDays(1);
        LocalDateTime forthDateStart = LocalDateTime.now().minusDays(4);
        LocalDateTime forthDateEnd = LocalDateTime.now().minusDays(3);
        LocalDateTime fifthDateStart = LocalDateTime.now().minusDays(2);
        LocalDateTime fifthDateEnd = LocalDateTime.now().minusDays(1);

        Booking firstBooking = new Booking(1, forthDateStart, forthDateEnd, item,
                secondUser, Status.APPROVED.toString());
        Booking secondBooking = new Booking(2, fifthDateStart, fifthDateEnd, item,
                secondUser, Status.APPROVED.toString());
        Booking thirdBooking = new Booking(3, thirdDateStart, thirdDateEnd, item,
                secondUser, Status.APPROVED.toString());
        Booking forthBooking = new Booking(4, firstDateStart, firstDateEnd, item,
                secondUser, Status.APPROVED.toString());
        Booking fifthBooking = new Booking(5, secondDateStart, secondDateEnd, item,
                secondUser, Status.APPROVED.toString());
        bookingRepository.save(firstBooking);
        bookingRepository.save(secondBooking);
        bookingRepository.save(thirdBooking);
        bookingRepository.save(forthBooking);
        bookingRepository.save(fifthBooking);

        Booking nextBooking = bookingRepository.findNextBooking(1, 1);
        assertThat(nextBooking.getId(), equalTo(4));
        assertThat(nextBooking.getStart(), equalTo(firstDateStart));
        assertThat(nextBooking.getEnd(), equalTo(firstDateEnd));
        assertThat(nextBooking.getItem().getId(), equalTo(1));
        assertThat(nextBooking.getItem().getName(), equalTo("Отвертка"));
        assertThat(nextBooking.getBooker().getId(), equalTo(2));
        assertThat(nextBooking.getBooker().getName(), equalTo("secondUser"));
        assertThat(nextBooking.getStatus(), equalTo(Status.APPROVED.toString()));

    }

    @Test
    void findBookingByItemIdAndByBookerId() {
        User user = new User(1, "user", "user@mail.ru");
        User secondUser = new User(2, "secondUser", "secondUser@mail.ru");
        userRepository.save(user);
        userRepository.save(secondUser);

        Item item = new Item(1, "Отвертка", "Хорошая отвертка", true,
                1, null);
        itemRepository.save(item);
        itemRepository.save(item);

        LocalDateTime firstDateStart = LocalDateTime.now().minusDays(5);
        LocalDateTime firstDateEnd = LocalDateTime.now().minusDays(6);
        LocalDateTime secondDateStart = LocalDateTime.now().minusDays(4);
        LocalDateTime secondDateEnd = LocalDateTime.now().minusDays(3);

        Booking firstBooking = new Booking(1, firstDateStart, firstDateEnd,
                item, secondUser, Status.APPROVED.toString());
        Booking secondBooking = new Booking(2, secondDateStart, secondDateEnd,
                item, secondUser, Status.APPROVED.toString());
        bookingRepository.save(firstBooking);
        bookingRepository.save(secondBooking);

        List<Booking> bookings = bookingRepository.findBookingByItemIdAndByBookerId(1, 2);
        assertThat(bookings.size(), equalTo(2));
        for (Booking booking : bookings) {
            if (booking.getId().equals(1)) {
                assertThat(booking.getId(), equalTo(1));
                assertThat(booking.getStart(), equalTo(firstDateStart));
                assertThat(booking.getEnd(), equalTo(firstDateEnd));
                assertThat(booking.getItem().getId(), equalTo(1));
                assertThat(booking.getItem().getName(), equalTo("Отвертка"));
                assertThat(booking.getBooker().getId(), equalTo(2));
                assertThat(booking.getBooker().getName(), equalTo("secondUser"));
                assertThat(booking.getStatus(), equalTo(Status.APPROVED.toString()));
            } else if (booking.getId().equals(2)) {
                assertThat(booking.getId(), equalTo(2));
                assertThat(booking.getStart(), equalTo(secondDateStart));
                assertThat(booking.getEnd(), equalTo(secondDateEnd));
                assertThat(booking.getItem().getId(), equalTo(1));
                assertThat(booking.getItem().getName(), equalTo("Отвертка"));
                assertThat(booking.getBooker().getId(), equalTo(2));
                assertThat(booking.getBooker().getName(), equalTo("secondUser"));
                assertThat(booking.getStatus(), equalTo(Status.APPROVED.toString()));
            }
        }
    }
}

