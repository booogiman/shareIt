package ru.practicum.shareit.booking;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.error.*;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Service
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;

    private final UserRepository userRepository;


//    @Autowired
//    public BookingServiceImpl(BookingRepository bookingRepository, ItemRepository itemRepository, UserRepository userRepository) {
//        this.bookingRepository = bookingRepository;
//        this.itemRepository = itemRepository;
//        this.userRepository = userRepository;
//    }

    @Override
    public ReturnedBookingDto add(Integer userId, ResultingBookingDto resultingBookingDto) {
        Item item = itemRepository.findById(resultingBookingDto.getItemId())
                .orElseThrow(() -> new ItemNotFoundException("Вещь с id " + resultingBookingDto.getItemId() + " не найдена"));
        User booker = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Пользователь с id " + userId + " не найден"));
        Booking booking = BookingMapper.toBooking(resultingBookingDto);
        booking.setStatus(Status.WAITING.toString());
        booking.setBooker(booker);
        booking.setItem(item);
        if (item.getOwnerId().equals(userId)) {
            throw new InvalidAccessException("Владелец вещи не может ее бронировать");
        }
        if (!itemRepository.existsItemById(booking.getItem().getId())) {
            throw new ItemNotFoundException("Вещь с id " + booking.getItem().getId() + " не найдена");
        }
        if (!item.getAvailable()) {
            throw new InvalidAvailableException("Вещь забронирована");
        }
        if (!userRepository.existsUserById(userId)) {
            throw new UserNotFoundException("Пользователь с id " + userId + " не найден");
        }
        if (!itemRepository.existsItemById(item.getId())) {
            throw new ItemNotFoundException("Вещь с id " + item.getId() + " не найдена");
        }
        if (booking.getStart().isBefore(LocalDateTime.now())) {
            throw new InvalidDateException("Дата старта не может быть в прошлом");
        }
        if (booking.getEnd().isBefore(LocalDateTime.now())) {
            throw new InvalidDateException("Дата конца не может быть в прошлом");
        }
        if (booking.getStart().isAfter(booking.getEnd())) {
            throw new InvalidDateException("Дата начала не может быть в позже даты конца");
        }
        return BookingMapper.toReturnedBookingDto(bookingRepository.save(booking));
    }

    @Override
    public ReturnedBookingDto patch(Integer userId, Integer bookingId, Boolean approved) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BookingNotFoundException("Бронирование с id " + bookingId + " не найдено"));
        Item item = itemRepository.findById(booking.getItem().getId())
                .orElseThrow(() -> new ItemNotFoundException("Вещь с id " + booking.getItem().getId() + " не найдена"));
        if (!userId.equals(item.getOwnerId())) {
            throw new InvalidAccessException(
                    "Подтверждение или отклонение запроса на бронирование может быть выполнено только владельцем вещи");
        }
        if (!booking.getStatus().equals("WAITING")) {
            throw new InvalidStatusException("Статус бронирования должен принимать значение \"WAITING\"");
        }
        if (approved) {
            booking.setStatus(Status.APPROVED.toString());
        } else {
            booking.setStatus(Status.REJECTED.toString());
        }
        return BookingMapper.toReturnedBookingDto(bookingRepository.save(booking));

    }

    @Override
    public ReturnedBookingDto getById(Integer userId, Integer bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BookingNotFoundException("Бронирование с id " + bookingId + " не найдено"));
        Item item = itemRepository.findById(booking.getItem().getId())
                .orElseThrow(() -> new ItemNotFoundException("Вещь с id " + booking.getItem().getId() + " не найдена"));
        if (!(userId.equals(item.getOwnerId())) && !(userId.equals(booking.getBooker().getId()))) {
            throw new InvalidAccessException(
                    "Получить данные о конкретном бронировании может либо автор бронирования, либо владелей вещи");
        }
        return BookingMapper.toReturnedBookingDto(bookingRepository.findById(bookingId).orElse(new Booking()));
    }

    @Override
    public List<ReturnedBookingDto> getAllBookingsByOwnerId(Integer userId, String state) {
        State enumState = State.valueOf(state);
        if (enumState == State.ALL) {
            return BookingMapper.toBookingDtoList(
                    bookingRepository.findByBookerIdOrderByStartDesc(userId));
        }
        if (enumState == State.CURRENT) {
            return BookingMapper.toBookingDtoList(
                    bookingRepository.findByBookerIdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(
                            userId, LocalDateTime.now(), LocalDateTime.now()));
        }
        if (enumState == State.PAST) {
            return BookingMapper.toBookingDtoList(
                    bookingRepository.findByBookerIdAndStartIsBeforeAndEndIsBeforeOrderByStartDesc(
                            userId, LocalDateTime.now(), LocalDateTime.now()));
        }
        if (enumState == State.FUTURE) {
            return BookingMapper.toBookingDtoList(
                    bookingRepository.findByBookerIdAndStartIsAfterAndEndIsAfterOrderByStartDesc(
                            userId, LocalDateTime.now(), LocalDateTime.now()));
        }
        if (enumState == State.WAITING) {
            return BookingMapper.toBookingDtoList(
                    bookingRepository.findByBookerIdAndStatusContainsOrderByStartDesc(
                            userId, Status.WAITING.toString()));
        }
        if (enumState == State.REJECTED) {
            return BookingMapper.toBookingDtoList(
                    bookingRepository.findByBookerIdAndStatusContainsOrderByStartDesc(
                            userId, Status.REJECTED.toString()));
        }
        return new ArrayList<>();
    }

    @Override
    public List<ReturnedBookingDto> getAllBookingsForAllItemsByOwnerId(Integer userId, String state) {
        State enumState = State.valueOf(state);
        if (enumState == State.ALL) {
           return BookingMapper.toBookingDtoList(bookingRepository.findAllUsersBookings(userId));
        }
        if (enumState == State.CURRENT) {
            return BookingMapper.toBookingDtoList(
                    bookingRepository.findAllCurrentUsersBookings(userId, LocalDateTime.now()));
        }
        if (enumState == State.PAST) {
            return BookingMapper.toBookingDtoList(bookingRepository.findAllPastUsersBookings(
                    userId, LocalDateTime.now(), LocalDateTime.now()));
        }
        if (enumState == State.FUTURE) {
            return BookingMapper.toBookingDtoList(bookingRepository.finnAllFutureUsersBookings(
                    userId, LocalDateTime.now(), LocalDateTime.now()));
        }
        if (enumState == State.WAITING) {
            return BookingMapper.toBookingDtoList(
                    bookingRepository.findAllUsersBookingsWithStatus(userId, Status.WAITING.toString()));
        }
        if (enumState == State.REJECTED) {
            return BookingMapper.toBookingDtoList(
                    bookingRepository.findAllUsersBookingsWithStatus(userId, Status.REJECTED.toString()));
        }
        return new ArrayList<>();
    }
}
