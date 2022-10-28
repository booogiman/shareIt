package ru.practicum.shareit.requests;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.error.*;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;
    private final UserRepository userRepository;

    private final ItemRepository itemRepository;

    @Autowired
    public RequestServiceImpl(RequestRepository requestRepository,
                              UserRepository userRepository, ItemRepository itemRepository) {
        this.requestRepository = requestRepository;
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
    }

    @Override
    public ItemRequestDto add(Integer userId, ItemRequestDto itemRequestDto) {
        if (!userRepository.existsUserById(userId)) {
            throw new InvalidUserIdException("Пользователь с id " + userId + " не найден");
        }
        if (itemRequestDto.getDescription() == null) {
            throw new InvalidItemRequestException("Текст запроса не задан");
        }
        itemRequestDto.setCreated(LocalDateTime.now());
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Пользователь с id " + userId + " не найден"));
        ItemRequest itemRequest = RequestMapper.toItemRequest(itemRequestDto);
        itemRequest.setRequester(user);
        return RequestMapper.toItemRequestDto(requestRepository.save(itemRequest));
    }

    @Override
    public ItemRequestDto getById(Integer userId, Integer requestId) {
        if (!userRepository.existsUserById(userId)) {
            throw new UserNotFoundException("Пользователь с id " + userId + " не найден");
        }
        List<Item> items = itemRepository.findAllByRequestId(requestId);
        ItemRequest itemRequest = requestRepository.findById(requestId)
                .orElseThrow(() -> new ItemRequestNotFoundException("Запрос с id " + requestId + " не найден"));
        ItemRequestDto itemRequestDto = RequestMapper.toItemRequestDto(itemRequest);
        itemRequestDto.setItems(ItemMapper.toItemDtoList(items));
        return itemRequestDto;
    }

    @Override
    public Collection<ItemRequestDto> getAllUsersRequests(Integer userId) {
        if (!userRepository.existsUserById(userId)) {
            throw new InvalidItemRequestParamException("Параметры затроса не могут отсутствовать");
        }
        List<ItemRequestDto> itemRequestDtoList = RequestMapper.toItemRequestDtoList(
                requestRepository.getAllByRequesterIdOrderByCreated(userId));
        for (ItemRequestDto itemRequest : itemRequestDtoList) {
            List<Item> items = itemRepository.findAllByRequestId(itemRequest.getId());
            itemRequest.setItems(ItemMapper.toItemDtoList(items));
        }
        return itemRequestDtoList;
    }

    @Override
    public Collection<ItemRequestDto> getAllRequests(Integer userId, Integer from, Integer size) {
        if (from == null && size == null) {
            return new ArrayList<>();
        }
        if (size == 0 || size < 0) {
            throw new InvalidItemRequestParamException(
                    "Размер страницы не может быть равен 0 и не может быть отрицательным");
        }
        if (from < 0) {
            throw new InvalidItemRequestParamException("Параметр from не может быть отрицательным");
        }
        PageRequest pageRequest = PageRequest.of(from, size, Sort.by("created"));
        Page<ItemRequest> itemRequests = requestRepository.findByIdIsNot(userId, pageRequest);
        List<ItemRequestDto> itemRequestDtoList = itemRequests.stream()
                .map(i -> RequestMapper.toItemRequestDto(i))
                .collect(Collectors.toList());
        for (ItemRequestDto itemRequestDto : itemRequestDtoList) {
            List<Item> items = itemRepository.findAllByRequestId(itemRequestDto.getId());
            itemRequestDto.setItems(ItemMapper.toItemDtoList(items));
        }
        return itemRequestDtoList;
    }
}
