package ru.practicum.shareit.requests;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;

    @Autowired
    public RequestServiceImpl(RequestRepository requestRepository) {
        this.requestRepository = requestRepository;
    }

    @Override
    public ItemRequestDto add(ItemRequestDto itemRequest) {
        return requestRepository.add(RequestMapper.toItemRequest(itemRequest));
    }

    @Override
    public ItemRequestDto update(ItemRequestDto itemRequest) {
        return requestRepository.update(RequestMapper.toItemRequest(itemRequest));
    }

    @Override
    public ItemRequestDto getById(Integer id) {
        return requestRepository.getById(id);
    }

    @Override
    public Collection<ItemRequestDto> getAll() {
        return requestRepository.getAll().values();
    }

    @Override
    public Boolean deleteById(Integer id) {
        return requestRepository.deleteById(id);
    }
}
