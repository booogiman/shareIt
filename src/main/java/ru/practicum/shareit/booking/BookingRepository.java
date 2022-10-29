package ru.practicum.shareit.booking;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Integer> {

    Page<Booking> findByBookerIdOrderByStartDesc(Integer bookerId, Pageable pageable);

    Page<Booking> findByBookerIdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(
            Integer bookerId, LocalDateTime dateOne, LocalDateTime dateTwo, Pageable pageable);

    Page<Booking> findByBookerIdAndStartIsAfterAndEndIsAfterOrderByStartDesc(
            Integer bookerId, LocalDateTime dateOne, LocalDateTime dateTwo, Pageable pageable);

    Page<Booking> findByBookerIdAndStartIsBeforeAndEndIsBeforeOrderByStartDesc(
            Integer bookerId, LocalDateTime dateOne, LocalDateTime dateTwo, Pageable pageable);

    Page<Booking> findByBookerIdAndStatusContainsOrderByStartDesc(Integer bookerId, String status, Pageable pageable);

    Boolean existsByBookerIdAndItemIdAndEndIsAfter(Integer bookerId, Integer itemId, LocalDateTime end);

    @Query("select b from Booking b Inner Join Item i on b.item.id = i.id " +
            "Where i.ownerId = ?1 ")
    Page<Booking> findAllUsersBookings(Integer ownerId, Pageable pageable);

    @Query("select b from Booking b Inner Join Item i on b.item.id = i.id " +
            "Where i.ownerId = ?1 and ?2 between b.start and b.end ")
    Page<Booking> findAllCurrentUsersBookings(Integer ownerId, LocalDateTime date, Pageable pageable);

    @Query("select b from Booking b Inner Join Item i on b.item.id = i.id " +
            "Where i.ownerId = ?1 and ?2 >= b.end and ?3 >= b.start ")
    Page<Booking> findAllPastUsersBookings(Integer ownerId, LocalDateTime dateOne, LocalDateTime dateTwo, Pageable pageable);

    @Query("select b from Booking b Inner Join Item i on b.item.id = i.id " +
            "Where i.ownerId = ?1 and ?2 <= b.end and ?3 <= b.start ")
    Page<Booking> findAllFutureUsersBookings(Integer ownerId, LocalDateTime dateOne, LocalDateTime dateTwo, Pageable pageable);

    @Query("select b from Booking b Inner Join Item i on b.item.id = i.id " +
            "Where i.ownerId = ?1 and b.status like ?2 ")
    Page<Booking> findAllUsersBookingsWithStatus(Integer ownerId, String query, Pageable pageable);

    @Query(value = "select * from bookings b inner join items i on i.id = b.item_id "
            + "Where b.item_id = ?1 and b.end_date_time < now() and b.status = 'APPROVED' and i.owner_id = ?2 "
            + "order by now() - b.end_date_time asc "
            + "limit 1",
            nativeQuery = true)
    Booking findLastBooking(Integer itemId, Integer ownerId);

    @Query(value = "select * from bookings b inner join items i on i.id = b.item_id "
            + "Where b.item_id = ?1 and b.start_date_time > now() and b.status = 'APPROVED' and i.owner_id = ?2 "
            + "order by b.start_date_time - now() asc "
            + "limit 1",
            nativeQuery = true)
    Booking findNextBooking(Integer itemId, Integer ownerId);

    @Query(value = "select * from bookings " +
            "where item_id = ?1 and end_date_time < now() and booker_id = ?2 and status = 'APPROVED'", nativeQuery = true)
    List<Booking> findBookingByItemIdAndByBookerId(Integer itemId, Integer bookerId);

}
