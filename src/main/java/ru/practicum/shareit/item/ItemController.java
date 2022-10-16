package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

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
    public Collection<ItemDto> getAll(@RequestHeader("X-Sharer-User-Id") Integer userId) {
        return itemService.getAll(userId);
    }

    @GetMapping("/search")
    public List<ItemDto> search(@RequestParam(required = true) String text) {
        return itemService.search(text);
    }

    @PostMapping("{itemId}/comment")
    public CommentDto addComment(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                 @PathVariable Integer itemId,
                                 @RequestBody CommentDto comment) {
        return itemService.addComment(userId, itemId, comment);
    }

}
