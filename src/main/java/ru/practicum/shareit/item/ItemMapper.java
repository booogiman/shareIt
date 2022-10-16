package ru.practicum.shareit.item;

import java.util.HashMap;

public class ItemMapper {

    public static ItemDto toItemDto(Item item) {
        return new ItemDto(item.getId(), item.getName(),
                item.getDescription(), item.getAvailable(),
                item.getOwner() != null ? item.getOwner().getId() : null,
                item.getRequest() != null ? item.getRequest().getId() : null);
    }

    public static HashMap<Integer, ItemDto> toItemDtoMap(HashMap<Integer, Item> items) {
        HashMap<Integer, ItemDto> itemDtos = new HashMap<>();
        for (Item item : items.values()) {
            itemDtos.put(item.getId(), toItemDto(item));
        }
        return itemDtos;
    }

    public static Item toItem(ItemDto itemDto) {
        return new Item(itemDto.getId(), itemDto.getName(), itemDto.getDescription(),
                itemDto.getAvailable(), null, null);
    }
}
