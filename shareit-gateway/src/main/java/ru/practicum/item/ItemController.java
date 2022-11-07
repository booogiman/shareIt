package ru.practicum.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Validated
@RestController
@RequestMapping("/items")
public class ItemController {
    private final ItemClient itemClient;

    @Autowired
    public ItemController(ItemClient itemClient) {
        this.itemClient = itemClient;
    }


    @PostMapping
    public Object add(@RequestHeader("X-Sharer-User-Id") Integer userId,
                      @RequestBody @Valid ItemDto item) {
        return itemClient.add(userId, item);
    }

    @PatchMapping("/{id}")
    public Object patch(@RequestHeader("X-Sharer-User-Id") Integer userId,
                        @PathVariable Integer id,
                        @RequestBody ItemDto item) {
        return itemClient.patch(userId, id, item);
    }

    @GetMapping("{itemId}")
    public Object get(@RequestHeader("X-Sharer-User-Id") Integer userId,
                      @PathVariable Integer itemId) {
        return itemClient.getById(userId, itemId);

    }

    @GetMapping
    public Object getAll(@RequestHeader("X-Sharer-User-Id") Integer userId,
                         @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                         @Positive @RequestParam(defaultValue = "10") Integer page) {
        return itemClient.getAll(userId, from, page);
    }

    @GetMapping("/search")
    public Object search(@RequestParam(required = true) String text,
                         @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                         @Positive @RequestParam(defaultValue = "10") Integer page) {
        return itemClient.search(text, from, page);
    }

    @PostMapping("{itemId}/comment")
    public Object addComment(@RequestHeader("X-Sharer-User-Id") Integer userId,
                             @PathVariable Integer itemId,
                             @RequestBody @Valid CommentDto comment) {
        return itemClient.addComment(userId, itemId, comment);
    }

}
