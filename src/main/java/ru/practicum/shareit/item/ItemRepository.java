package ru.practicum.shareit.item;

import java.util.List;

public interface ItemRepository {

    ItemDto add(Item item);

    ItemDto getById(Integer itemId);

    List<ItemDto> getAll(Integer userId);

    Boolean deleteById(Integer userId, Integer itemId);

    ItemDto patch(Integer itemId, Item item);

    List<ItemDto> search(String query);
}
