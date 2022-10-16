package ru.practicum.shareit.requests;

import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class InMemoryRequestRepository implements RequestRepository {

    HashMap<Integer, ItemRequest> itemRequests = new HashMap<>();

    private Integer idMax = 0;

    private Integer getIdMax() {
        idMax = idMax + 1;
        return idMax;
    }

    @Override
    public ItemRequestDto add(ItemRequest itemRequest) {
        itemRequest.setId(getIdMax());
        itemRequests.put(itemRequest.getId(), itemRequest);
        return RequestMapper.toItemRequestDto(itemRequest);
    }

    @Override
    public ItemRequestDto update(ItemRequest itemRequest) {
        itemRequests.put(itemRequest.getId(), itemRequest);
        return RequestMapper.toItemRequestDto(itemRequest);
    }

    @Override
    public ItemRequestDto getById(Integer id) {
        return RequestMapper.toItemRequestDto(itemRequests.get(id));
    }

    @Override
    public HashMap<Integer, ItemRequestDto> getAll() {
        return RequestMapper.toItemRequestMap(itemRequests);
    }

    @Override
    public Boolean deleteById(Integer id) {
        itemRequests.remove(id);
        return true;
    }
}
