package ru.practicum.shareit.requests;

import java.util.Collection;

public interface RequestService {

    ItemRequestDto add(ItemRequestDto itemRequest);

    ItemRequestDto update(ItemRequestDto itemRequest);

    ItemRequestDto getById(Integer id);

    Collection<ItemRequestDto> getAll();

    Boolean deleteById(Integer id);
}
