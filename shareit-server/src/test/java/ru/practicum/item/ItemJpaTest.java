package ru.practicum.item;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.user.User;
import ru.practicum.user.UserRepository;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ItemJpaTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ItemRepository itemRepository;

    @Test
    void search() {
        User user = new User(1, "user", "user@mail.ru");
        User secondUser = new User(2, "secondUser", "secondUser@mail.ru");
        userRepository.save(user);
        userRepository.save(secondUser);

        Item item = new Item(1, "Аккумулятор для машины", "Хороший аккумулятор",
                true, 1, null);
        Item secondItem = new Item(2, "Аккумулятор для телефона", "Новый аккумулятор",
                true, 2, null);
        itemRepository.save(item);
        itemRepository.save(secondItem);

        Page<Item> items = itemRepository.search("акКум", PageRequest.of(0, 5));
        assertThat(items.toList().size(), equalTo(2));
        for (Item foundedItem : items) {
            if (foundedItem.getId().equals(1)) {
                assertThat(foundedItem.getId(), equalTo(1));
                assertThat(foundedItem.getName(), equalTo("Аккумулятор для машины"));
                assertThat(foundedItem.getDescription(), equalTo("Хороший аккумулятор"));
                assertThat(foundedItem.getAvailable(), equalTo(true));
                assertThat(foundedItem.getOwnerId(), equalTo(1));
            } else if (foundedItem.getId().equals(2)) {
                assertThat(foundedItem.getId(), equalTo(2));
                assertThat(foundedItem.getName(), equalTo("Аккумулятор для телефона"));
                assertThat(foundedItem.getDescription(), equalTo("Новый аккумулятор"));
                assertThat(foundedItem.getAvailable(), equalTo(true));
                assertThat(foundedItem.getOwnerId(), equalTo(2));
            }
        }
    }
}
