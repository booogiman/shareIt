package ru.practicum.item;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@Getter
@Setter
public class CommentDto {

    private Integer id;
    @NotEmpty
    private String text;

    private ItemDto item;

    private String authorName;

    private LocalDateTime created;

    public CommentDto(Integer id, String text, ItemDto item, String authorName, LocalDateTime created) {
        this.id = id;
        this.text = text;
        this.item = item;
        this.authorName = authorName;
        this.created = created;
    }
}
