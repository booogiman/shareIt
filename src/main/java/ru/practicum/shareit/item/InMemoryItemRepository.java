package ru.practicum.shareit.item;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
public class InMemoryItemRepository implements ItemRepository {

    HashMap<Integer, Item> items = new HashMap<>();

    private Integer idMax = 0;

    private Integer getIdMax() {
        idMax = idMax + 1;
        return idMax;
    }

    @Override
    public ItemDto add(Item item) {
        item.setId(getIdMax());
        items.put(item.getId(), item);
        return ItemMapper.toItemDto(items.get(item.getId()));
    }

    @Override
    public ItemDto getById(Integer itemId) {
        return ItemMapper.toItemDto(items.get(itemId));
    }

    @Override
    public List<ItemDto> getAll(Integer userId) {
        List<ItemDto> itemDtos = new ArrayList<>();
        for (Item item : items.values()) {
            if (item.getOwner().getId().equals(userId)) {
                itemDtos.add(ItemMapper.toItemDto(item));
            }
        }
        return itemDtos;
    }

    @Override
    public Boolean deleteById(Integer userId, Integer itemId) {
        return null;
    }

    @Override
    public ItemDto patch(Integer itemId, Item item) {
        Item foundedItem = items.get(itemId);
        if (item.getName() != null) {
            foundedItem.setName(item.getName());
        }
        if (item.getDescription() != null) {
            foundedItem.setDescription(item.getDescription());
        }
        if (item.getAvailable() != null) {
            foundedItem.setAvailable(item.getAvailable());
        }
        return ItemMapper.toItemDto(foundedItem);
    }

    @Override
    public List<ItemDto> search(String query) {
        List<ItemDto> foundedItems = new ArrayList<>();
        for (Item item : items.values()) {
            if (item.getAvailable() && (item.getName().toLowerCase().contains(query) ||
                    item.getDescription().toLowerCase().contains(query))) {
                foundedItems.add(ItemMapper.toItemDto(item));
            }
        }
        return foundedItems;
    }
}
