package ru.practicum.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;

    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @PostMapping
    public ItemDto add(@RequestHeader("X-Sharer-User-Id") Integer userId,
                       @RequestBody ItemDto item) {
        return itemService.add(userId, item);
    }

    @PatchMapping("/{id}")
    public ItemDto patch(@RequestHeader("X-Sharer-User-Id") Integer userId,
                         @PathVariable Integer id,
                         @RequestBody ItemDto item) {
        return itemService.patch(userId, id, item);
    }

    @GetMapping("{itemId}")
    public ItemDto get(@RequestHeader("X-Sharer-User-Id") Integer userId,
                       @PathVariable Integer itemId) {
        return itemService.getById(userId, itemId);

    }

    @GetMapping
    public Collection<ItemDto> getAll(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                      @RequestParam(defaultValue = "0", required = false) Integer from,
                                      @RequestParam(defaultValue = "10", required = false) Integer page) {
        return itemService.getAll(userId, from, page);
    }

    @GetMapping("/search")
    public Collection<ItemDto> search(@RequestParam(required = true) String text,
                                @RequestParam(defaultValue = "0", required = false) Integer from,
                                @RequestParam(defaultValue = "10", required = false) Integer page) {
        return itemService.search(text, from, page);
    }

    @PostMapping("{itemId}/comment")
    public CommentDto addComment(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                 @PathVariable Integer itemId,
                                 @RequestBody CommentDto comment) {
        return itemService.addComment(userId, itemId, comment);
    }

}
