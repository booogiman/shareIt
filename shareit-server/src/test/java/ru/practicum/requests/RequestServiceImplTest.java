package ru.practicum.requests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import ru.practicum.error.InvalidUserIdException;
import ru.practicum.error.UserNotFoundException;
import ru.practicum.item.Item;
import ru.practicum.item.ItemRepository;
import ru.practicum.user.User;
import ru.practicum.user.UserRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

class RequestServiceImplTest {

    RequestService requestService;
    RequestRepository requestRepository = Mockito.mock(RequestRepository.class);
    ItemRepository itemRepository = Mockito.mock(ItemRepository.class);
    UserRepository userRepository = Mockito.mock(UserRepository.class);
    User user = new User(1, "user", "user@mail.ru");
    List<Item> items = List.of(
            new Item(1, "Отвертка", "Хорошая отвертка", true, 1, 2),
            new Item(2, "Паяльник", "Надежный паяльник", true, 1, 3));
    List<ItemRequest> itemRequestList = List.of(
            new ItemRequest(1, "Хотел бы поспользоваться щеткой для обуви", user, LocalDateTime.now()),
            new ItemRequest(2, "Хотел бы поспользоваться паяльником", user, LocalDateTime.now()));
    ItemRequest itemRequest = new ItemRequest(1, "Хотел бы поспользоваться щеткой для обуви", user,
            LocalDateTime.now());
    ItemRequestDto itemRequestDto = new ItemRequestDto(1, "Хотел бы поспользоваться щеткой для обуви",
            1, LocalDateTime.now(), null);

    PageImpl<ItemRequest> itemRequestPage = new PageImpl<>(List.of(
            new ItemRequest(1, "Хотел бы поспользоваться щеткой для обуви", user, LocalDateTime.now()),
            new ItemRequest(2, "Хотел бы поспользоваться паяльником", user, LocalDateTime.now())));

    @BeforeEach
    void beforeEach() {
        requestService = new RequestServiceImpl(requestRepository, userRepository, itemRepository);
    }

    @Test
    void add() {
        Mockito
                .when(userRepository.existsUserById(Mockito.anyInt()))
                .thenReturn(true);
        Mockito
                .when(userRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.ofNullable(user));
        Mockito
                .when(requestRepository.save(Mockito.any(ItemRequest.class)))
                .thenReturn(itemRequest);
        ItemRequestDto foundedItemRequestDto = requestService.add(1, itemRequestDto);
        assertThat(foundedItemRequestDto.getId(), equalTo(itemRequestDto.getId()));
        assertThat(foundedItemRequestDto.getDescription(), equalTo(itemRequestDto.getDescription()));
        assertThat(foundedItemRequestDto.getRequester(), equalTo(itemRequestDto.getRequester()));
    }

    @Test
    void addUserNotExists() {
        Mockito
                .when(userRepository.existsUserById(Mockito.anyInt()))
                .thenThrow(new InvalidUserIdException("Пользователь с данным id не найден"));
        final InvalidUserIdException exception = Assertions.assertThrows(InvalidUserIdException.class,
                () -> requestService.add(1, itemRequestDto));
        Assertions.assertEquals("Пользователь с данным id не найден", exception.getMessage());
    }

    @Test
    void addUserNotFound() {
        Mockito
                .when(userRepository.existsUserById(Mockito.anyInt()))
                .thenReturn(true);
        Mockito
                .when(userRepository.findById(Mockito.anyInt()))
                .thenThrow(new UserNotFoundException("Пользователь с данным id не найден"));
        final UserNotFoundException exception = Assertions.assertThrows(UserNotFoundException.class,
                () -> requestService.add(1, itemRequestDto));
        Assertions.assertEquals("Пользователь с данным id не найден", exception.getMessage());
    }

    @Test
    void getById() {
        Mockito
                .when(userRepository.existsUserById(Mockito.anyInt()))
                .thenReturn(true);
        Mockito
                .when(itemRepository.findAllByRequestId(Mockito.anyInt()))
                .thenReturn(items);
        Mockito
                .when(requestRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.ofNullable(itemRequest));
        ItemRequestDto foundedItemRequestDto = requestService.getById(1, 1);
        assertThat(foundedItemRequestDto.getId(), equalTo(itemRequestDto.getId()));
        assertThat(foundedItemRequestDto.getDescription(), equalTo(itemRequestDto.getDescription()));
        assertThat(foundedItemRequestDto.getRequester(), equalTo(itemRequestDto.getRequester()));
    }

    @Test
    void getAllUsersRequests() {
        Mockito
                .when(userRepository.existsUserById(Mockito.anyInt()))
                .thenReturn(true);
        Mockito
                .when(requestRepository.getAllByRequesterIdOrderByCreated(Mockito.anyInt()))
                .thenReturn(itemRequestList);
        Mockito
                .when(itemRepository.findAllByRequestId(Mockito.anyInt()))
                .thenReturn(items);
        Collection<ItemRequestDto> itemRequestDtoList = requestService.getAllUsersRequests(1);
        for (ItemRequestDto foundedItemRequestDto : itemRequestDtoList) {
            if (foundedItemRequestDto.getId().equals(1)) {
                assertThat(foundedItemRequestDto.getId(), equalTo(1));
                assertThat(foundedItemRequestDto.getDescription(), equalTo("Хотел бы поспользоваться щеткой для обуви"));
            } else if (foundedItemRequestDto.getId().equals(2)) {
                assertThat(foundedItemRequestDto.getId(), equalTo(2));
                assertThat(foundedItemRequestDto.getDescription(), equalTo("Хотел бы поспользоваться паяльником"));
            }
        }
    }

    @Test
    void getAllRequests() {
        Mockito
                .when(requestRepository.findByIdIsNot(Mockito.anyInt(), Mockito.any(Pageable.class)))
                .thenReturn(itemRequestPage);
        Mockito
                .when(itemRepository.findAllByRequestId(Mockito.anyInt()))
                .thenReturn(items);
        Collection<ItemRequestDto> itemRequestDtoList = requestService.getAllRequests(1, 0,5);
        for (ItemRequestDto foundedItemRequestDto : itemRequestDtoList) {
            if (foundedItemRequestDto.getId().equals(1)) {
                assertThat(foundedItemRequestDto.getId(), equalTo(1));
                assertThat(foundedItemRequestDto.getDescription(), equalTo("Хотел бы поспользоваться щеткой для обуви"));
            } else if (foundedItemRequestDto.getId().equals(2)) {
                assertThat(foundedItemRequestDto.getId(), equalTo(2));
                assertThat(foundedItemRequestDto.getDescription(), equalTo("Хотел бы поспользоваться паяльником"));
            }
        }
    }
}