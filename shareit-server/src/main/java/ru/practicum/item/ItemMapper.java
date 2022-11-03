package ru.practicum.item;

import java.util.ArrayList;
import java.util.List;

public class ItemMapper {

    public static ItemDto toItemDto(Item item) {
        return new ItemDto(item.getId(), item.getName(),
                item.getDescription(), item.getAvailable(),
                item.getOwnerId() != null ? item.getOwnerId() : null,
                item.getRequestId() != null ? item.getRequestId() : null,
                null, null);
    }

    public static List<ItemDto> toItemDtoList(List<Item> items) {
        List<ItemDto> itemDtos = new ArrayList<>();
        for (Item item : items) {
            itemDtos.add(toItemDto(item));
        }
        return itemDtos;
    }

    public static Item toItem(ItemDto itemDto) {
        return new Item(itemDto.getId(), itemDto.getName(), itemDto.getDescription(),
                itemDto.getAvailable(), null,
                itemDto.getRequestId() != null ? itemDto.getRequestId() : null);
    }
}
