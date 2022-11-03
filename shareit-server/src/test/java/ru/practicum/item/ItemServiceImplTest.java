package ru.practicum.item;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import ru.practicum.booking.Booking;
import ru.practicum.booking.BookingMapper;
import ru.practicum.booking.BookingRepository;
import ru.practicum.booking.Status;
import ru.practicum.error.IdNotFoundException;
import ru.practicum.error.UserNotFoundException;
import ru.practicum.user.User;
import ru.practicum.user.UserRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

class ItemServiceImplTest {

    ItemService itemService;
    BookingRepository bookingRepository = Mockito.mock(BookingRepository.class);
    ItemRepository itemRepository = Mockito.mock(ItemRepository.class);
    UserRepository userRepository = Mockito.mock(UserRepository.class);
    CommentRepository commentRepository = Mockito.mock(CommentRepository.class);
    Item item = new Item(1, "Отвертка", "Хорошая отвертка", true, 1, null);
    User user = new User(1, "user", "firstuser@mail.ru");
    User secondUser = new User(2, "second User", "secondUser@mail.ru");
    User thirdUser = new User(2, "second User", "secondUser@mail.ru");
    List<User> users = List.of(new User(1, "user", "firstuser@mail.ru"),
            new User(2, "secondUser", "secondUser@mail.ru"),
            new User(3, "thirdUser", "thirdUser@mail.ru"));
    List<Comment> comments = List.of(
            new Comment(1, "Очень хорошая отвертка", item, secondUser, LocalDateTime.now()),
            new Comment(2, "Надежная отвертка", item, thirdUser, LocalDateTime.now()));
    LocalDateTime date = LocalDateTime.of(2022, 8, 20, 13, 12);
    Booking booking = new Booking(1, date, date.plusDays(1), item, user, Status.WAITING.toString());
    Booking secondBooking = new Booking(2, date.plusDays(2), date.plusDays(3),
            item, user, Status.WAITING.toString());
    ItemDto itemDto = new ItemDto(1, "Отвертка", "Хорошая отвертка", true, 1,
            null, BookingMapper.toItemBookingDto(booking), BookingMapper.toItemBookingDto(secondBooking));
    Comment comment = new Comment(1, "Очень хорошая отвертка", item, secondUser, LocalDateTime.now());
    CommentDto commentDto = new CommentDto(1, "Очень хорошая отвертка", ItemMapper.toItemDto(item),
            secondUser.getName(), LocalDateTime.now());
    List<Booking> bookings = List.of(new Booking(1, date, date.plusDays(1), item, user, Status.WAITING.toString()));
    PageImpl<Item> itemPage = new PageImpl<>(List.of(
            new Item(1, "Отвертка", "Хорошая отвертка", true, 1, null)));

    @BeforeEach
    void beforeEach() {
        itemService = new ItemServiceImpl(itemRepository, userRepository, commentRepository, bookingRepository);
    }

    @Test
    void add() {
        Mockito
                .when(userRepository.findAll())
                .thenReturn(users);
        Mockito
                .when(userRepository.existsUserById(Mockito.anyInt()))
                .thenReturn(true);
        Mockito
                .when(userRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.ofNullable(user));
        Mockito
                .when(itemRepository.save(Mockito.any(Item.class)))
                .thenReturn(item);
        ItemDto foundedItemDto = itemService.add(1, itemDto);
        assertThat(foundedItemDto.getId(), equalTo(itemDto.getId()));
        assertThat(foundedItemDto.getName(), equalTo(itemDto.getName()));
        assertThat(foundedItemDto.getDescription(), equalTo(itemDto.getDescription()));
        assertThat(foundedItemDto.getAvailable(), equalTo(itemDto.getAvailable()));
    }

    @Test
    void addFailIdNotFound() {
        Mockito
                .when(userRepository.findAll())
                .thenThrow(new IdNotFoundException("Ни один пользователь не добавлен в систему"));
        final IdNotFoundException exception = Assertions.assertThrows(
                IdNotFoundException.class, () -> itemService.add(1, itemDto));
        Assertions.assertEquals("Ни один пользователь не добавлен в систему", exception.getMessage());
    }

    @Test
    void addFailUserNotExists() {
        Mockito
                .when(userRepository.findAll())
                .thenReturn(users);
        Mockito
                .when(userRepository.existsUserById(Mockito.anyInt()))
                .thenThrow(new IdNotFoundException("Ни один пользователь не добавлен в систему"));
        final IdNotFoundException exception = Assertions.assertThrows(
                IdNotFoundException.class, () -> itemService.add(1, itemDto));
        Assertions.assertEquals("Ни один пользователь не добавлен в систему", exception.getMessage());
    }

    @Test
    void addFailUserNotFound() {
        Mockito
                .when(userRepository.findAll())
                .thenReturn(users);
        Mockito
                .when(userRepository.existsUserById(Mockito.anyInt()))
                .thenReturn(true);
        Mockito
                .when(userRepository.findById(Mockito.anyInt()))
                .thenThrow(new UserNotFoundException("Пользователь с данным id не найден в базе"));
        final UserNotFoundException exception = Assertions.assertThrows(
                UserNotFoundException.class, () -> itemService.add(1, itemDto));
        Assertions.assertEquals("Пользователь с данным id не найден в базе", exception.getMessage());
    }

    @Test
    void getById() {
        Mockito
                .when(userRepository.existsUserById(Mockito.anyInt()))
                .thenReturn(true);
        Mockito
                .when(itemRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.ofNullable(item));
        Mockito
                .when(bookingRepository.findLastBooking(Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(booking);
        Mockito
                .when((bookingRepository.findNextBooking(Mockito.anyInt(), Mockito.anyInt())))
                .thenReturn(secondBooking);
        Mockito
                .when(commentRepository.findAllByItemId(Mockito.anyInt()))
                .thenReturn(comments);
        ItemDto foundedItemDto = itemService.getById(1, 1);
        assertThat(foundedItemDto.getId(), equalTo(itemDto.getId()));
        assertThat(foundedItemDto.getName(), equalTo(itemDto.getName()));
        assertThat(foundedItemDto.getDescription(), equalTo(itemDto.getDescription()));
        assertThat(foundedItemDto.getAvailable(), equalTo(itemDto.getAvailable()));
        assertThat(foundedItemDto.getOwnerId(), equalTo(itemDto.getOwnerId()));
        assertThat(foundedItemDto.getLastBooking().getId(), equalTo(itemDto.getLastBooking().getId()));
        assertThat(foundedItemDto.getLastBooking().getBookerId(), equalTo(itemDto.getLastBooking().getBookerId()));
        assertThat(foundedItemDto.getNextBooking().getId(), equalTo(itemDto.getNextBooking().getId()));
        assertThat(foundedItemDto.getNextBooking().getBookerId(), equalTo(itemDto.getNextBooking().getBookerId()));
    }

    @Test
    void getAll() {
        Mockito
                .when(userRepository.existsUserById(Mockito.anyInt()))
                .thenReturn(true);
        Mockito
                .when(itemRepository.getAllByOwnerIdOrderById(Mockito.anyInt(), Mockito.any(Pageable.class)))
                .thenReturn(itemPage);
        Mockito
                .when(bookingRepository.findLastBooking(Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(booking);
        Mockito
                .when((bookingRepository.findNextBooking(Mockito.anyInt(), Mockito.anyInt())))
                .thenReturn(secondBooking);
        Collection<ItemDto> itemDtos = itemService.getAll(1, 0, 5);
        for (ItemDto foundedItemDto : itemDtos) {
            if (foundedItemDto.getId().equals(1)) {
                assertThat(foundedItemDto.getId(), equalTo(itemDto.getId()));
                assertThat(foundedItemDto.getName(), equalTo(itemDto.getName()));
                assertThat(foundedItemDto.getDescription(), equalTo(itemDto.getDescription()));
                assertThat(foundedItemDto.getAvailable(), equalTo(itemDto.getAvailable()));
            }
        }
    }

    @Test
    void patch() {
        Mockito
                .when(itemRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.ofNullable(item));
        Mockito
                .when(itemRepository.save(Mockito.any(Item.class)))
                .thenReturn(item);
        ItemDto foundedItemDto = itemService.patch(1, 1, itemDto);
        assertThat(foundedItemDto.getId(), equalTo(itemDto.getId()));
        assertThat(foundedItemDto.getName(), equalTo(itemDto.getName()));
        assertThat(foundedItemDto.getDescription(), equalTo(itemDto.getDescription()));
        assertThat(foundedItemDto.getAvailable(), equalTo(itemDto.getAvailable()));
    }

    @Test
    void search() {
        Mockito
                .when(itemRepository.search(Mockito.anyString(), Mockito.any(Pageable.class)))
                .thenReturn(itemPage);
        Collection<ItemDto> itemDtos = itemService.search("Отвертка", 0, 5);
        for (ItemDto foundedItemDto : itemDtos) {
            if (foundedItemDto.getId().equals(1)) {
                assertThat(foundedItemDto.getId(), equalTo(itemDto.getId()));
                assertThat(foundedItemDto.getName(), equalTo(itemDto.getName()));
                assertThat(foundedItemDto.getDescription(), equalTo(itemDto.getDescription()));
                assertThat(foundedItemDto.getAvailable(), equalTo(itemDto.getAvailable()));
            }
        }
    }

    @Test
    void addComment() {
        Mockito
                .when(itemRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.ofNullable(item));
        Mockito
                .when(userRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.ofNullable(user));
        Mockito
                .when(bookingRepository.findBookingByItemIdAndByBookerId(Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(bookings);
        Mockito
                .when(bookingRepository.existsByBookerIdAndItemIdAndEndIsAfter(Mockito.anyInt(), Mockito.anyInt(),
                        Mockito.any(LocalDateTime.class)))
                .thenReturn(true);
        Mockito
                .when(commentRepository.save(Mockito.any(Comment.class)))
                .thenReturn(comment);
        CommentDto foundedCommentDto = itemService.addComment(1,1, commentDto);
        assertThat(foundedCommentDto.getId(), equalTo(commentDto.getId()));
        assertThat(foundedCommentDto.getText(), equalTo(commentDto.getText()));
        assertThat(foundedCommentDto.getItem().getId(), equalTo(commentDto.getItem().getId()));
        assertThat(foundedCommentDto.getAuthorName(), equalTo(commentDto.getAuthorName()));
    }
}