package ru.practicum.booking;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import ru.practicum.error.InvalidAccessException;
import ru.practicum.error.ItemNotFoundException;
import ru.practicum.error.UserNotFoundException;
import ru.practicum.item.Item;
import ru.practicum.item.ItemDto;
import ru.practicum.item.ItemRepository;
import ru.practicum.user.User;
import ru.practicum.user.UserDto;
import ru.practicum.user.UserRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

class BookingServiceImplTest {

//    BookingService bookingService;
    BookingRepository bookingRepository = Mockito.mock(BookingRepository.class);
    ItemRepository itemRepository = Mockito.mock(ItemRepository.class);
    UserRepository userRepository = Mockito.mock(UserRepository.class);
    BookingServiceImpl bookingService = new BookingServiceImpl(bookingRepository, itemRepository,
                                            userRepository);
    LocalDateTime date = LocalDateTime.of(2022, 11, 04, 13, 12);
    ResultingBookingDto resultingBookingDto = new ResultingBookingDto(
            1, date, date.plusDays(1), 1, 2, Status.WAITING.toString());
    ItemDto itemDto = new ItemDto(1, "Отвертка", "Хорошая отвертка", true, 1,
            null, null, null);
    UserDto userDto = new UserDto(1, "user", "user@mail.ru");
    User user = new User(1, "user", "user@mail.ru");
    User secondUser = new User(2, "second User", "secondUser@mail.ru");
    ReturnedBookingDto returnedBookingDto = new ReturnedBookingDto(
            1, date, date.plusDays(1), itemDto, userDto, Status.WAITING.toString());
    Item item = new Item(1, "Отвертка", "Хорошая отвертка", true, 1, 2);
    Booking booking = new Booking(1, date, date.plusDays(1), item, user, Status.WAITING.toString());
    PageImpl<Booking> pageAll = new PageImpl<Booking>(List.of(
            new Booking(1, date, date.plusDays(1), null,
                    null, Status.WAITING.toString()),
            new Booking(2, date.plusDays(1), date.plusDays(2), null,
                    null, Status.WAITING.toString()),
            new Booking(3, date.minusMonths(1), LocalDateTime.now().plusMinutes(2), null,
                    null, Status.REJECTED.toString()),
            new Booking(4, date.minusMonths(2), date.minusMonths(1), null,
                    null, Status.REJECTED.toString())));
    PageImpl<Booking> pageCurrent = new PageImpl<>(List.of(
            new Booking(3, date.minusMonths(1), LocalDateTime.now().plusMinutes(2), null,
                    null, Status.REJECTED.toString())));
    PageImpl<Booking> pageFuture = new PageImpl<>(List.of(
            new Booking(1, date, date.plusDays(1), null, null, Status.WAITING.toString()),
            new Booking(2, date.plusDays(1), date.plusDays(2),
                    null, null, Status.WAITING.toString())));
    PageImpl<Booking> pagePast = new PageImpl<>(List.of(
            new Booking(4, date.minusMonths(2), date.minusMonths(1), null,
                    null, Status.REJECTED.toString())));
    PageImpl<Booking> pageWaiting = new PageImpl<>(List.of(
            new Booking(1, date, date.plusDays(1), null, null, Status.WAITING.toString()),
            new Booking(2, date.plusDays(1), date.plusDays(2), null,
                    null, Status.WAITING.toString())));
    PageImpl<Booking> pageRejected = new PageImpl<>(List.of(
            new Booking(3, date.minusMonths(1), LocalDateTime.now().plusMinutes(2), null,
                    null, Status.REJECTED.toString()),
            new Booking(4, date.minusMonths(2), date.minusMonths(1), null,
                    null, Status.REJECTED.toString())));

//    @BeforeEach
//    void beforeEach() {
//        bookingService = new BookingServiceImpl(bookingRepository, itemRepository,
//                userRepository);
//    }

    @Test
    void add() {
        Mockito
                .when(itemRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.ofNullable(item));
        Mockito
                .when(userRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.ofNullable(secondUser));
        Mockito
                .when(userRepository.existsUserById(Mockito.anyInt()))
                .thenReturn(true);
        Mockito
                .when(itemRepository.existsItemById(Mockito.anyInt()))
                .thenReturn(true);
        Mockito
                .when(bookingRepository.save(Mockito.any(Booking.class)))
                .thenReturn(booking);
        ReturnedBookingDto foundedReturnedBookingDto = bookingService.add(2, resultingBookingDto);
        assertThat(foundedReturnedBookingDto.getId(), equalTo(returnedBookingDto.getId()));
        assertThat(foundedReturnedBookingDto.getStart(), equalTo(returnedBookingDto.getStart()));
        assertThat(foundedReturnedBookingDto.getEnd(), equalTo(returnedBookingDto.getEnd()));
        assertThat(foundedReturnedBookingDto.getStatus(), equalTo(returnedBookingDto.getStatus()));
    }

    @Test
    void failAddUserNotFound() {
        Mockito
                .when(itemRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.ofNullable(item));
        Mockito
                .when(userRepository.findById(Mockito.anyInt()))
                .thenThrow(new InvalidAccessException("Пользователь с данным id не найден"));
        final InvalidAccessException exception = Assertions.assertThrows(InvalidAccessException.class,
                () -> bookingService.add(1, resultingBookingDto));
        Assertions.assertEquals("Пользователь с данным id не найден", exception.getMessage());
    }

    @Test
    void failAddItemNotFound() {
        Mockito
                .when(itemRepository.findById(Mockito.anyInt()))
                .thenThrow(new ItemNotFoundException("Вещь с данным id не найдена"));
        final ItemNotFoundException exception = Assertions.assertThrows(ItemNotFoundException.class,
                () -> bookingService.add(2, resultingBookingDto));
        Assertions.assertEquals("Вещь с данным id не найдена", exception.getMessage());

    }

    @Test
    void failAddUserNotExists() {
        Mockito
                .when(itemRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.ofNullable(item));
        Mockito
                .when(userRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.ofNullable(secondUser));
        Mockito
                .when(itemRepository.existsItemById(Mockito.anyInt()))
                .thenReturn(true);
        Mockito
                .when(userRepository.existsUserById(Mockito.anyInt()))
                .thenThrow(new UserNotFoundException("Пользователь с данным id не найден в базе"));
        final UserNotFoundException exception = Assertions.assertThrows(UserNotFoundException.class,
                () -> bookingService.add(2, resultingBookingDto));
        Assertions.assertEquals("Пользователь с данным id не найден в базе", exception.getMessage());
    }

    @Test
    void failAddItemNotExists() {
        Mockito
                .when(itemRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.ofNullable(item));
        Mockito
                .when(userRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.ofNullable(secondUser));
        Mockito
                .when(itemRepository.existsItemById(Mockito.anyInt()))
                .thenThrow(new ItemNotFoundException("Вещь с данным id не найдена в базе"));

        final ItemNotFoundException exception = Assertions.assertThrows(ItemNotFoundException.class,
                () -> bookingService.add(2, resultingBookingDto));
        Assertions.assertEquals("Вещь с данным id не найдена в базе", exception.getMessage());
    }

    @Test
    void patch() {
        Mockito
                .when(itemRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.ofNullable(item));
        Mockito
                .when(bookingRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.ofNullable(booking));
        Mockito
                .when(bookingRepository.save(Mockito.any(Booking.class)))
                .thenReturn(booking);
        ReturnedBookingDto foundedReturnedBookingDto = bookingService.patch(1, 1, true);
        assertThat(foundedReturnedBookingDto.getId(), equalTo(returnedBookingDto.getId()));
        assertThat(foundedReturnedBookingDto.getStart(), equalTo(returnedBookingDto.getStart()));
        assertThat(foundedReturnedBookingDto.getEnd(), equalTo(returnedBookingDto.getEnd()));
        assertThat(foundedReturnedBookingDto.getStatus(), equalTo(Status.APPROVED.toString()));
    }

    @Test
    void getById() {
        Mockito
                .when(itemRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.ofNullable(item));
        Mockito
                .when(bookingRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.ofNullable(booking));
        ReturnedBookingDto foundedReturnedBookingDto = bookingService.getById(1, 1);
        assertThat(foundedReturnedBookingDto.getId(), equalTo(returnedBookingDto.getId()));
        assertThat(foundedReturnedBookingDto.getStart(), equalTo(returnedBookingDto.getStart()));
        assertThat(foundedReturnedBookingDto.getEnd(), equalTo(returnedBookingDto.getEnd()));
        assertThat(foundedReturnedBookingDto.getStatus(), equalTo(returnedBookingDto.getStatus()));
    }

    @Test
    void getAllBookingsByOwnerIdAllState() {
        Mockito
                .when(userRepository.existsUserById(Mockito.anyInt()))
                .thenReturn(true);
        Mockito
                .when(bookingRepository.findByBookerIdOrderByStartDesc(Mockito.anyInt(),
                        Mockito.any(Pageable.class)))
                .thenReturn(pageAll);
        Collection<ReturnedBookingDto> returnedBookingDtos = bookingService.getAllBookingsByOwnerId(
                1, "ALL", 0, 5);
        for (ReturnedBookingDto foundedReturnedBookingDto : returnedBookingDtos) {
            if (foundedReturnedBookingDto.getId().equals(1)) {
                assertThat(foundedReturnedBookingDto.getId(), equalTo(1));
                assertThat(foundedReturnedBookingDto.getStart(), equalTo(date));
                assertThat(foundedReturnedBookingDto.getEnd(), equalTo(date.plusDays(1)));
                assertThat(foundedReturnedBookingDto.getStatus(), equalTo(Status.WAITING.toString()));
            } else if (foundedReturnedBookingDto.getId().equals(2)) {
                assertThat(foundedReturnedBookingDto.getId(), equalTo(2));
                assertThat(foundedReturnedBookingDto.getStart(), equalTo(date.plusDays(1)));
                assertThat(foundedReturnedBookingDto.getEnd(), equalTo(date.plusDays(2)));
                assertThat(foundedReturnedBookingDto.getStatus(), equalTo(Status.WAITING.toString()));
            } else if (foundedReturnedBookingDto.getId().equals(3)) {
                assertThat(foundedReturnedBookingDto.getId(), equalTo(3));
                assertThat(foundedReturnedBookingDto.getStart(), equalTo(date.minusMonths(1)));
                assertThat(foundedReturnedBookingDto.getStatus(), equalTo(Status.REJECTED.toString()));
            }
        }
    }

    @Test
    void getAllBookingsByOwnerIdCurrentState() {
        Mockito
                .when(userRepository.existsUserById(Mockito.anyInt()))
                .thenReturn(true);
        Mockito
                .when(bookingRepository.findByBookerIdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(
                        Mockito.anyInt(), Mockito.any(LocalDateTime.class),
                        Mockito.any(LocalDateTime.class), Mockito.any(Pageable.class)))
                .thenReturn(pageCurrent);
        Collection<ReturnedBookingDto> returnedBookingDtos = bookingService.getAllBookingsByOwnerId(
                1, "CURRENT", 0, 5);
        for (ReturnedBookingDto foundedReturnedBookingDto : returnedBookingDtos) {
            if (foundedReturnedBookingDto.getId().equals(3)) {
                assertThat(foundedReturnedBookingDto.getId(), equalTo(3));
                assertThat(foundedReturnedBookingDto.getStart(), equalTo(date.minusMonths(1)));
                assertThat(foundedReturnedBookingDto.getStatus(), equalTo(Status.REJECTED.toString()));
            }
        }
    }

    @Test
    void getAllBookingsByOwnerIdFutureState() {
        Mockito
                .when(userRepository.existsUserById(Mockito.anyInt()))
                .thenReturn(true);
        Mockito
                .when(bookingRepository.findByBookerIdAndStartIsAfterAndEndIsAfterOrderByStartDesc(
                        Mockito.anyInt(), Mockito.any(LocalDateTime.class),
                        Mockito.any(LocalDateTime.class), Mockito.any(Pageable.class)))
                .thenReturn(pageFuture);
        Collection<ReturnedBookingDto> returnedBookingDtos = bookingService.getAllBookingsByOwnerId(
                1, "FUTURE", 0, 5);
        for (ReturnedBookingDto foundedReturnedBookingDto : returnedBookingDtos) {
            if (foundedReturnedBookingDto.getId().equals(1)) {
                assertThat(foundedReturnedBookingDto.getId(), equalTo(1));
                assertThat(foundedReturnedBookingDto.getStart(), equalTo(date));
                assertThat(foundedReturnedBookingDto.getEnd(), equalTo(date.plusDays(1)));
                assertThat(foundedReturnedBookingDto.getStatus(), equalTo(Status.WAITING.toString()));
            } else if (foundedReturnedBookingDto.getId().equals(2)) {
                assertThat(foundedReturnedBookingDto.getId(), equalTo(2));
                assertThat(foundedReturnedBookingDto.getStart(), equalTo(date.plusDays(1)));
                assertThat(foundedReturnedBookingDto.getEnd(), equalTo(date.plusDays(2)));
                assertThat(foundedReturnedBookingDto.getStatus(), equalTo(Status.WAITING.toString()));
            }
        }
    }

    @Test
    void getAllBookingsByOwnerIdPastState() {
        Mockito
                .when(userRepository.existsUserById(Mockito.anyInt()))
                .thenReturn(true);
        Mockito
                .when(bookingRepository.findByBookerIdAndStartIsBeforeAndEndIsBeforeOrderByStartDesc(
                        Mockito.anyInt(), Mockito.any(LocalDateTime.class),
                        Mockito.any(LocalDateTime.class), Mockito.any(Pageable.class)))
                .thenReturn(pagePast);
        Collection<ReturnedBookingDto> returnedBookingDtos = bookingService.getAllBookingsByOwnerId(
                1, "PAST", 0, 5);
        for (ReturnedBookingDto foundedReturnedBookingDto : returnedBookingDtos) {
            if (foundedReturnedBookingDto.getId().equals(4)) {
                assertThat(foundedReturnedBookingDto.getId(), equalTo(4));
                assertThat(foundedReturnedBookingDto.getStart(), equalTo(date.minusMonths(2)));
                assertThat(foundedReturnedBookingDto.getEnd(), equalTo(date.minusMonths(1)));
                assertThat(foundedReturnedBookingDto.getStatus(), equalTo(Status.REJECTED.toString()));
            }
        }
    }

    @Test
    void getAllBookingsByOwnerIdWaitingState() {
        Mockito
                .when(userRepository.existsUserById(Mockito.anyInt()))
                .thenReturn(true);
        Mockito
                .when(bookingRepository.findByBookerIdAndStatusContainsOrderByStartDesc(
                        Mockito.anyInt(), Mockito.any(String.class), Mockito.any(Pageable.class)))
                .thenReturn(pageWaiting);
        Collection<ReturnedBookingDto> returnedBookingDtos = bookingService.getAllBookingsByOwnerId(
                1, "WAITING", 0, 5);
        for (ReturnedBookingDto foundedReturnedBookingDto : returnedBookingDtos) {
            if (foundedReturnedBookingDto.getId().equals(1)) {
                assertThat(foundedReturnedBookingDto.getId(), equalTo(1));
                assertThat(foundedReturnedBookingDto.getStart(), equalTo(date));
                assertThat(foundedReturnedBookingDto.getEnd(), equalTo(date.plusDays(1)));
                assertThat(foundedReturnedBookingDto.getStatus(), equalTo(Status.WAITING.toString()));
            } else if (foundedReturnedBookingDto.getId().equals(2)) {
                assertThat(foundedReturnedBookingDto.getId(), equalTo(2));
                assertThat(foundedReturnedBookingDto.getStart(), equalTo(date.plusDays(1)));
                assertThat(foundedReturnedBookingDto.getEnd(), equalTo(date.plusDays(2)));
                assertThat(foundedReturnedBookingDto.getStatus(), equalTo(Status.WAITING.toString()));
            }
        }
    }

    @Test
    void getAllBookingsByOwnerIdRejectedState() {
        Mockito
                .when(userRepository.existsUserById(Mockito.anyInt()))
                .thenReturn(true);
        Mockito
                .when(bookingRepository.findByBookerIdAndStatusContainsOrderByStartDesc(
                        Mockito.anyInt(), Mockito.any(String.class), Mockito.any(Pageable.class)))
                .thenReturn(pageRejected);
        Collection<ReturnedBookingDto> returnedBookingDtos = bookingService.getAllBookingsByOwnerId(
                1, "REJECTED", 0, 5);
        for (ReturnedBookingDto foundedReturnedBookingDto : returnedBookingDtos) {
            if (foundedReturnedBookingDto.getId().equals(3)) {
                assertThat(foundedReturnedBookingDto.getId(), equalTo(3));
                assertThat(foundedReturnedBookingDto.getStart(), equalTo(date.minusMonths(1)));
                assertThat(foundedReturnedBookingDto.getStatus(), equalTo(Status.REJECTED.toString()));
            } else if (foundedReturnedBookingDto.getId().equals(4)) {
                assertThat(foundedReturnedBookingDto.getId(), equalTo(4));
                assertThat(foundedReturnedBookingDto.getStart(), equalTo(date.minusMonths(2)));
                assertThat(foundedReturnedBookingDto.getEnd(), equalTo(date.minusMonths(1)));
                assertThat(foundedReturnedBookingDto.getStatus(), equalTo(Status.REJECTED.toString()));
            }
        }
    }

    @Test
    void getAllBookingsForAllItemsByOwnerIdAllState() {
        Mockito
                .when(userRepository.existsUserById(Mockito.anyInt()))
                .thenReturn(true);
        Mockito
                .when(bookingRepository.findAllUsersBookings(Mockito.anyInt(), Mockito.any(Pageable.class)))
                .thenReturn(pageAll);
        Collection<ReturnedBookingDto> returnedBookingDtos = bookingService.getAllBookingsForAllItemsByOwnerId(
                1, "ALL", 0, 5);
        for (ReturnedBookingDto foundedReturnedBookingDto : returnedBookingDtos) {
            if (foundedReturnedBookingDto.getId().equals(1)) {
                assertThat(foundedReturnedBookingDto.getId(), equalTo(1));
                assertThat(foundedReturnedBookingDto.getStart(), equalTo(date));
                assertThat(foundedReturnedBookingDto.getEnd(), equalTo(date.plusDays(1)));
                assertThat(foundedReturnedBookingDto.getStatus(), equalTo(Status.WAITING.toString()));
            } else if (foundedReturnedBookingDto.getId().equals(2)) {
                assertThat(foundedReturnedBookingDto.getId(), equalTo(2));
                assertThat(foundedReturnedBookingDto.getStart(), equalTo(date.plusDays(1)));
                assertThat(foundedReturnedBookingDto.getEnd(), equalTo(date.plusDays(2)));
                assertThat(foundedReturnedBookingDto.getStatus(), equalTo(Status.WAITING.toString()));
            } else if (foundedReturnedBookingDto.getId().equals(3)) {
                assertThat(foundedReturnedBookingDto.getId(), equalTo(3));
                assertThat(foundedReturnedBookingDto.getStart(), equalTo(date.minusMonths(1)));
                assertThat(foundedReturnedBookingDto.getStatus(), equalTo(Status.REJECTED.toString()));
            }
        }
    }

    @Test
    void getAllBookingsForAllItemsByOwnerIdCurrentState() {
        Mockito
                .when(userRepository.existsUserById(Mockito.anyInt()))
                .thenReturn(true);
        Mockito
                .when(bookingRepository.findAllCurrentUsersBookings(
                        Mockito.anyInt(), Mockito.any(LocalDateTime.class), Mockito.any(Pageable.class)))
                .thenReturn(pageCurrent);
        Collection<ReturnedBookingDto> returnedBookingDtos = bookingService.getAllBookingsForAllItemsByOwnerId(
                1, "CURRENT", 0, 5);
        for (ReturnedBookingDto foundedReturnedBookingDto : returnedBookingDtos) {
            if (foundedReturnedBookingDto.getId().equals(3)) {
                assertThat(foundedReturnedBookingDto.getId(), equalTo(3));
                assertThat(foundedReturnedBookingDto.getStart(), equalTo(date.minusMonths(1)));
                assertThat(foundedReturnedBookingDto.getStatus(), equalTo(Status.REJECTED.toString()));
            }
        }
    }

    @Test
    void getAllBookingsForAllItemsByOwnerIdPastState() {
        Mockito
                .when(userRepository.existsUserById(Mockito.anyInt()))
                .thenReturn(true);
        Mockito
                .when(bookingRepository.findAllPastUsersBookings(
                        Mockito.anyInt(), Mockito.any(LocalDateTime.class),
                        Mockito.any(LocalDateTime.class),  Mockito.any(Pageable.class)))
                .thenReturn(pagePast);
        Collection<ReturnedBookingDto> returnedBookingDtos = bookingService.getAllBookingsForAllItemsByOwnerId(
                1, "PAST", 0, 5);
        for (ReturnedBookingDto foundedReturnedBookingDto : returnedBookingDtos) {
            if (foundedReturnedBookingDto.getId().equals(4)) {
                assertThat(foundedReturnedBookingDto.getId(), equalTo(4));
                assertThat(foundedReturnedBookingDto.getStart(), equalTo(date.minusMonths(2)));
                assertThat(foundedReturnedBookingDto.getEnd(), equalTo(date.minusMonths(1)));
                assertThat(foundedReturnedBookingDto.getStatus(), equalTo(Status.REJECTED.toString()));
            }
        }
    }

    @Test
    void getAllBookingsForAllItemsByOwnerIdFutureState() {
        Mockito
                .when(userRepository.existsUserById(Mockito.anyInt()))
                .thenReturn(true);
        Mockito
                .when(bookingRepository.finnAllFutureUsersBookings(
                        Mockito.anyInt(), Mockito.any(LocalDateTime.class),
                        Mockito.any(LocalDateTime.class),  Mockito.any(Pageable.class)))
                .thenReturn(pagePast);
        Collection<ReturnedBookingDto> returnedBookingDtos = bookingService.getAllBookingsForAllItemsByOwnerId(
                1, "FUTURE", 0, 5);
        for (ReturnedBookingDto foundedReturnedBookingDto : returnedBookingDtos) {
            if (foundedReturnedBookingDto.getId().equals(1)) {
                assertThat(foundedReturnedBookingDto.getId(), equalTo(1));
                assertThat(foundedReturnedBookingDto.getStart(), equalTo(date));
                assertThat(foundedReturnedBookingDto.getEnd(), equalTo(date.plusDays(1)));
                assertThat(foundedReturnedBookingDto.getStatus(), equalTo(Status.WAITING.toString()));
            } else if (foundedReturnedBookingDto.getId().equals(2)) {
                assertThat(foundedReturnedBookingDto.getId(), equalTo(2));
                assertThat(foundedReturnedBookingDto.getStart(), equalTo(date.plusDays(1)));
                assertThat(foundedReturnedBookingDto.getEnd(), equalTo(date.plusDays(2)));
                assertThat(foundedReturnedBookingDto.getStatus(), equalTo(Status.WAITING.toString()));
            }
        }
    }

    @Test
    void getAllBookingsForAllItemsByOwnerIdWaitingState() {
        Mockito
                .when(userRepository.existsUserById(Mockito.anyInt()))
                .thenReturn(true);
        Mockito
                .when(bookingRepository.findAllUsersBookingsWithStatus(
                        Mockito.anyInt(), Mockito.any(String.class), Mockito.any(Pageable.class)))
                .thenReturn(pageWaiting);
        Collection<ReturnedBookingDto> returnedBookingDtos = bookingService.getAllBookingsForAllItemsByOwnerId(
                1, "WAITING", 0, 5);
        for (ReturnedBookingDto foundedReturnedBookingDto : returnedBookingDtos) {
            if (foundedReturnedBookingDto.getId().equals(1)) {
                assertThat(foundedReturnedBookingDto.getId(), equalTo(1));
                assertThat(foundedReturnedBookingDto.getStart(), equalTo(date));
                assertThat(foundedReturnedBookingDto.getEnd(), equalTo(date.plusDays(1)));
                assertThat(foundedReturnedBookingDto.getStatus(), equalTo(Status.WAITING.toString()));
            } else if (foundedReturnedBookingDto.getId().equals(2)) {
                assertThat(foundedReturnedBookingDto.getId(), equalTo(2));
                assertThat(foundedReturnedBookingDto.getStart(), equalTo(date.plusDays(1)));
                assertThat(foundedReturnedBookingDto.getEnd(), equalTo(date.plusDays(2)));
                assertThat(foundedReturnedBookingDto.getStatus(), equalTo(Status.WAITING.toString()));
            }
        }
    }

    @Test
    void getAllBookingsForAllItemsByOwnerIdRejectedState() {
        Mockito
                .when(userRepository.existsUserById(Mockito.anyInt()))
                .thenReturn(true);
        Mockito
                .when(bookingRepository.findAllUsersBookingsWithStatus(
                        Mockito.anyInt(), Mockito.any(String.class), Mockito.any(Pageable.class)))
                .thenReturn(pageRejected);
        Collection<ReturnedBookingDto> returnedBookingDtos = bookingService.getAllBookingsForAllItemsByOwnerId(
                1, "REJECTED", 0, 5);
        for (ReturnedBookingDto foundedReturnedBookingDto : returnedBookingDtos) {
            if (foundedReturnedBookingDto.getId().equals(3)) {
                assertThat(foundedReturnedBookingDto.getId(), equalTo(3));
                assertThat(foundedReturnedBookingDto.getStart(), equalTo(date.minusMonths(1)));
                assertThat(foundedReturnedBookingDto.getStatus(), equalTo(Status.REJECTED.toString()));
            } else if (foundedReturnedBookingDto.getId().equals(4)) {
                assertThat(foundedReturnedBookingDto.getId(), equalTo(4));
                assertThat(foundedReturnedBookingDto.getStart(), equalTo(date.minusMonths(2)));
                assertThat(foundedReturnedBookingDto.getEnd(), equalTo(date.minusMonths(1)));
                assertThat(foundedReturnedBookingDto.getStatus(), equalTo(Status.REJECTED.toString()));
            }
        }
    }
}