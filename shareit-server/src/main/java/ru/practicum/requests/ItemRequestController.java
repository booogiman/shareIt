package ru.practicum.requests;

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
    public ItemRequestDto add(@RequestHeader("X-Sharer-User-Id") Integer userId,
                              @RequestBody ItemRequestDto itemRequest) {
        return requestService.add(userId, itemRequest);
    }

    @GetMapping
    public Collection<ItemRequestDto> getAllUsersRequests(@RequestHeader("X-Sharer-User-Id") Integer userId) {
        return requestService.getAllUsersRequests(userId);
    }

    @GetMapping("/all")
    public Collection<ItemRequestDto> getAllRequests(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                                     @RequestParam(required = false) Integer from,
                                                     @RequestParam(required = false) Integer size) {
        return requestService.getAllRequests(userId, from, size);
    }

    @GetMapping("{requestId}")
    public ItemRequestDto getRequestById(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                         @PathVariable Integer requestId) {
        return requestService.getById(userId, requestId);
    }
}
