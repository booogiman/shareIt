package ru.practicum.item;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Table(name = "items")
@Entity
@Getter
@Setter
@ToString
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String description;
    private Boolean available;
    @Column(name = "owner_id", nullable = false)
    private Integer ownerId;
    @Column(name = "request_id")
    private Integer requestId;

    public Item(Integer id, String name, String description, Boolean available, Integer ownerId, Integer requestId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.available = available;
        this.ownerId = ownerId;
        this.requestId = requestId;
    }

    public Item() {

    }
}
