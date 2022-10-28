package ru.practicum.shareit.item;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Integer> {

    Boolean existsItemById(Integer id);

    List<Item> getAllByOwnerIdOrderById(Integer id);

    @Query(" select i from Item i " +
            "where upper(i.name) like upper(concat('%', ?1, '%')) " +
            " or upper(i.description) like upper(concat('%', ?1, '%'))")
    Page<Item> search(String text, Pageable pageable);

    List<Item> findAllByRequestId(Integer requestId);

    Page<Item> getAllByOwnerIdOrderById(Integer id, Pageable pageable);

}
