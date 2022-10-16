package ru.practicum.shareit.requests;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping(path = "/requests")
public class ItemRequestController {

    private final RequestService requestService;

    @Autowired
    public ItemRequestController(RequestService requestService) {
        this.requestService = requestService;
    }

    @PostMapping
    public ItemRequestDto add(@RequestBody ItemRequestDto itemRequest) {
        return requestService.add(itemRequest);
    }

    @PutMapping
    public ItemRequestDto update(@RequestBody ItemRequestDto itemRequest) {
        return requestService.update(itemRequest);
    }

    @GetMapping("/{id}")
    public ItemRequestDto get(@PathVariable Integer id) {
        return requestService.getById(id);
    }

    @GetMapping
    public Collection<ItemRequestDto> getAll() {
        return requestService.getAll();
    }

    @DeleteMapping("/{id}")
    public Boolean delete(@PathVariable Integer id) {
        return requestService.deleteById(id);
    }
}
