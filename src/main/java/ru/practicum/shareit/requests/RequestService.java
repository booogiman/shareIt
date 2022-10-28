package ru.practicum.shareit.requests;

import java.util.Collection;

public interface RequestService {

    ItemRequestDto add(Integer userId, ItemRequestDto itemRequest);

    ItemRequestDto getById(Integer userId, Integer requestId);

    Collection<ItemRequestDto> getAllUsersRequests(Integer userId);

    Collection<ItemRequestDto> getAllRequests(Integer userId, Integer from, Integer size);

}
