package ru.practicum.requests;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.item.ItemDto;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class ItemRequestDto {
    private Integer id;
    @NotEmpty
    private String description;
    private Integer requester;
    private LocalDateTime created;

    private List<ItemDto> items;

    public ItemRequestDto(Integer id, String description, Integer requester, LocalDateTime created, List<ItemDto> items) {
        this.id = id;
        this.description = description;
        this.requester = requester;
        this.created = created;
        this.items = items;
    }
}
