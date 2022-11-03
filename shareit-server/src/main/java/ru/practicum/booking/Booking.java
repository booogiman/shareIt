package ru.practicum.booking;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.item.Item;
import ru.practicum.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Table(name = "bookings")
@Entity
@Getter
@Setter
@ToString
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "start_date_time", nullable = false)
    private LocalDateTime start;
    @Column(name = "end_date_time", nullable = false)
    private LocalDateTime end;
    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;
    @ManyToOne
    @JoinColumn(name = "booker_id")
    private User booker;
    @Column(nullable = false)
    private String status;

    public Booking(Integer id, LocalDateTime start, LocalDateTime end, Item item, User booker, String status) {
        this.id = id;
        this.start = start;
        this.end = end;
        this.item = item;
        this.booker = booker;
        this.status = status;
    }

    public Booking() {

    }
}


