package ru.practicum.server.jpa;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.practicum.server.item.Item;
import ru.practicum.server.item.ItemRepository;
import ru.practicum.server.user.User;
import ru.practicum.server.user.UserRepository;

import java.time.LocalDateTime;

@DataJpaTest
public class ItemDataJpaTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ItemRepository itemRepository;
    @Autowired

    private UserRepository userRepository;

    @Test
    void testItemRequestDto() throws Exception {
        LocalDateTime time = LocalDateTime.now().withNano(000000);

        User user = new User(1L, "John Doe", "johndoe@example.com");
        User savedUser = userRepository.save(user);

        Item item = new Item(1L, "Веник", "Домашний", savedUser, true, null);
        Item savedItem = itemRepository.save(item);

        Long savedItemId = savedItem.getId();

        Item retrievedItem = entityManager.find(Item.class, savedItemId);

        Assertions.assertThat(retrievedItem).isEqualTo(savedItem);
    }
}