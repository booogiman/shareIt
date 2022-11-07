package ru.practicum.requests;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Table(name = "item_requests")
@Entity
@Getter
@Setter
@ToString
public class ItemRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String description;
    @ManyToOne
    @JoinColumn(name = "requester_id")
    private User requester;
    private LocalDateTime created;

    public ItemRequest(Integer id, String description, User requester, LocalDateTime created) {
        this.id = id;
        this.description = description;
        this.requester = requester;
        this.created = created;
    }

    public ItemRequest() {

    }
}
