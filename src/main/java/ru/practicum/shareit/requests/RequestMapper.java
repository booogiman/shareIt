package ru.practicum.shareit.requests;

import java.util.ArrayList;
import java.util.List;

public class RequestMapper {
    public static ItemRequestDto toItemRequestDto(ItemRequest itemRequest) {
        return new ItemRequestDto(itemRequest.getId(), itemRequest.getDescription(),
                itemRequest.getRequester() != null ? itemRequest.getRequester().getId() : null,
                itemRequest.getCreated(), null);
    }

    public static List<ItemRequestDto> toItemRequestDtoList(List<ItemRequest> itemRequests) {
        List<ItemRequestDto> itemRequestDtoList = new ArrayList<>();
        for (ItemRequest itemRequest : itemRequests) {
            itemRequestDtoList.add(toItemRequestDto(itemRequest));
        }
        return itemRequestDtoList;
    }

    public static ItemRequest toItemRequest(ItemRequestDto itemRequestDto) {
        return new ItemRequest(itemRequestDto.getId(), itemRequestDto.getDescription(),
                null, itemRequestDto.getCreated());
    }
}
