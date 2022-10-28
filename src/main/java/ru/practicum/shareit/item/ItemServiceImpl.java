package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.error.*;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    private final BookingRepository bookingRepository;

    @Autowired
    public ItemServiceImpl(ItemRepository itemRepository, UserRepository userRepository,
                           CommentRepository commentRepository, BookingRepository bookingRepository) {
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
        this.bookingRepository = bookingRepository;
    }

    @Override
    public ItemDto add(Integer ownerId, ItemDto item) {
        if (ownerId == null) {
            throw new InvalidIdException("Ошибка id пользователя");
        }
        if (userRepository.findAll().isEmpty()) {
            throw new IdNotFoundException("Ни один пользователь не добавлен в систему");
        }
        if (item.getAvailable() == null) {
            throw new InvalidAvailableException("Не задан статус вещи");
        }
        if (item.getName() == null || item.getName().isBlank()) {
            throw new InvalidNameException("Не задано имя вещи");
        }
        if (item.getDescription() == null || item.getDescription().isBlank()) {
            throw new InvalidDescriptionException("Не задано описание вещи");
        }
        if (!userRepository.existsUserById(ownerId)) {
            throw new IdNotFoundException("Id пользователя не найден в базе");
        }
        Item createdItem = ItemMapper.toItem(item);
        User user = userRepository.findById(ownerId)
                .orElseThrow(() -> new UserNotFoundException("Пользователь с id " + ownerId + " не найден"));
        createdItem.setOwnerId(user.getId());
        return ItemMapper.toItemDto(itemRepository.save(createdItem));
    }

    @Override
    public ItemDto getById(Integer userId, Integer itemId) {
        if (!userRepository.existsUserById(userId)) {
            throw new UserNotFoundException("Пользователь с id " + userId + " не найден");
        }
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ItemNotFoundException("Вещь с id " + itemId + " не найдена"));
        ItemDto itemDto = ItemMapper.toItemDto(item);
        if ((bookingRepository.findLastBooking(itemId, userId)) != null && (
                bookingRepository.findNextBooking(itemId, userId) != null)) {
            itemDto.setLastBooking(BookingMapper.toItemBookingDto(bookingRepository.findLastBooking(itemId, userId)));
            itemDto.setNextBooking(BookingMapper.toItemBookingDto(bookingRepository.findNextBooking(itemId, userId)));
        }
        itemDto.setComments(CommentMapper.toCommentDtoList(commentRepository.findAllByItemId(itemId)));
        return itemDto;
    }

    @Override
    public Collection<ItemDto> getAll(Integer userId, Integer from, Integer size) {
        if (!userRepository.existsUserById(userId)) {
            throw new InvalidUserException("Пользователь не добавлен в систему");
        }
        PageRequest pageRequest = PageRequest.of(from, size);
        Page<Item> itemPage = itemRepository.getAllByOwnerIdOrderById(userId, pageRequest);
        List<ItemDto> itemDtos = itemPage.stream()
                .map(i -> ItemMapper.toItemDto(i))
                .collect(Collectors.toList());
        for (ItemDto itemDto : itemDtos) {
            if ((bookingRepository.findLastBooking(itemDto.getId(), userId)) != null
                    && (bookingRepository.findNextBooking(itemDto.getId(), userId) != null)) {
                itemDto.setLastBooking(BookingMapper.toItemBookingDto(
                        bookingRepository.findLastBooking(itemDto.getId(), userId)));
                itemDto.setNextBooking(BookingMapper.toItemBookingDto(
                        bookingRepository.findNextBooking(itemDto.getId(), userId)));
                itemDto.setComments(CommentMapper.toCommentDtoList(commentRepository.findAllByItemId(itemDto.getId())));
            }
        }
        return itemDtos;
    }

    @Override
    public ItemDto patch(Integer userId, Integer itemId, ItemDto item) {
        Item foundedItem = itemRepository
                .findById(itemId).orElseThrow(() -> new ItemNotFoundException("Вещь с id " + itemId + " не найдена"));
        if (userId == null) {
            throw new InvalidIdException("Ошибка id пользователя");
        }
        if (!foundedItem.getOwnerId().equals(userId)) {
            throw new InvalidUserException("Редактировать вещь может только ее владелец");
        }
        if (item.getName() != null) {
            foundedItem.setName(item.getName());
        }
        if (item.getDescription() != null) {
            foundedItem.setDescription(item.getDescription());
        }
        if (item.getAvailable() != null) {
            foundedItem.setAvailable(item.getAvailable());
        }
        return ItemMapper.toItemDto(itemRepository.save(foundedItem));
    }

    @Override
    public Collection<ItemDto> search(String query, Integer from, Integer size) {
        List<Item> items = new ArrayList<>();
        if (query.isBlank()) {
            return new ArrayList<>();
        }
        PageRequest pageRequest = PageRequest.of(from, size);
        Page<Item> itemPage = itemRepository.search(query, pageRequest);
        List<Item> foundedItems = itemPage.stream()
                .collect(Collectors.toList());
        for (Item item : foundedItems) {
            if (item.getAvailable()) {
                items.add(item);
            }
        }
        return ItemMapper.toItemDtoList(items);
    }

    @Override
    public CommentDto addComment(Integer userId, Integer itemId, CommentDto commentDto) {
        Comment createdComment = CommentMapper.toComment(commentDto);
        Item item = itemRepository
                .findById(itemId).orElseThrow(() -> new ItemNotFoundException("Вещь с id " + itemId + " не найдена"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Пользователь с id " + userId + " не найден"));
        createdComment.setItem(item);
        createdComment.setAuthor(user);
        createdComment.setCreated(LocalDateTime.now());
        if (createdComment.getText().isBlank()) {
            throw new InvalidCommentException("Комментарий не может быть пустым");
        }
        if (bookingRepository.findBookingByItemIdAndByBookerId(itemId, userId).isEmpty()) {
            throw new InvalidCommentException("Пользователь не брал данную вещь в аренду");
        }
        if (!bookingRepository.existsByBookerIdAndItemIdAndEndIsAfter(
                createdComment.getAuthor().getId(), createdComment.getItem().getId(), LocalDateTime.now())) {
            throw new InvalidCommentException("Оставлять отзыв может только тот пользователь, который брал " +
                    "вещь в аренду и только после окончания срока аренды");
        }
        return CommentMapper.toCommentDto(commentRepository.save(createdComment));
    }
}
