package ru.practicum.requests;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Validated
@RestController
@RequestMapping(path = "/requests")
public class ItemRequestController {

    private final ItemRequestClient itemRequestClient;

    @Autowired
    public ItemRequestController(ItemRequestClient itemRequestClient) {
        this.itemRequestClient = itemRequestClient;
    }


    @PostMapping
    public Object add(@RequestHeader("X-Sharer-User-Id") Integer userId,
                      @RequestBody @Valid ItemRequestDto itemRequest) {
        return itemRequestClient.add(userId, itemRequest);
    }

    @GetMapping
    public Object getAllUsersRequests(@RequestHeader("X-Sharer-User-Id") Integer userId) {
        return itemRequestClient.getAllUsersRequests(userId);
    }

    @GetMapping("/all")
    public Object getAllRequests(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                 @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                 @Positive @RequestParam(defaultValue = "10") Integer size) {
        return itemRequestClient.getAllRequests(userId, from, size);
    }

    @GetMapping("{requestId}")
    public Object getRequestById(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                 @PathVariable Integer requestId) {
        return itemRequestClient.getById(userId, requestId);
    }
}
