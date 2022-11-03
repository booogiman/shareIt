package ru.practicum.item;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ItemDto {
    private Integer id;
    @NotEmpty
    private String name;
    @NotEmpty
    private String description;
    @NotNull
    private Boolean available;
    private Integer ownerId;
    private Integer requestId;

    private ItemBookingDto lastBooking;

    private ItemBookingDto nextBooking;

    private List<CommentDto> comments = new ArrayList<>();

    public ItemDto(Integer id, String name, String description, Boolean available, Integer ownerId,
                   Integer requestId, ItemBookingDto lastBooking, ItemBookingDto nextBooking) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.available = available;
        this.ownerId = ownerId;
        this.requestId = requestId;
        this.lastBooking = lastBooking;
        this.nextBooking = nextBooking;
    }
}
