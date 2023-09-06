package ru.practicum.shareit.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoForItem;
import ru.practicum.shareit.item.*;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest(properties = {
        "spring.config.name=application-test",
        "spring.config.location=classpath:application-test.properties"
}, webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ItemServiceTest {

    private final ItemRepository itemRepository;

    private final BookingRepository bookingRepository;

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    private final UserService userService;
    private final ItemService itemService;

    @Test
    public void testCreate() {

        User user = new User();
        user.setEmail("test@test.com");
        user.setName("Test User");
        userRepository.save(user);

        ItemDto itemDto = new ItemDto();
        itemDto.setName("Test Item");
        itemDto.setDescription("Test Description");
        itemDto.setAvailable(true);

        Long userId = user.getId();

        ItemResponseDto itemResponseDto = itemService.create(itemDto, userId);

        assertNotNull(itemResponseDto);
        assertNotNull(itemResponseDto.getId());
        assertEquals(userId, itemResponseDto.getOwner().getId());
    }

    @Test
    public void testGetAll() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setName("Test User");
        userRepository.save(user);

        Item item1 = new Item();
        item1.setName("Item 1");
        item1.setDescription("Test Description");
        item1.setAvailable(true);
        item1.setOwner(user);
        itemRepository.save(item1);

        Item item2 = new Item();
        item2.setName("Item 2");
        item2.setDescription("Test Description 2");
        item2.setAvailable(true);
        item2.setOwner(user);
        itemRepository.save(item2);

        Long userId = user.getId();

        List<ItemResponseDto> itemResponseDtoList = itemService.getAll(userId);

        assertNotNull(itemResponseDtoList);
        assertEquals(2, itemResponseDtoList.size());
    }

    @Test
    public void testGetById() {

        User user = new User();
        user.setEmail("test@example.com");
        user.setName("Test User");
        userRepository.save(user);

        Item item = new Item();
        item.setName("Test Item");
        item.setDescription("Test Description");
        item.setAvailable(true);
        item.setOwner(user);
        itemRepository.save(item);

        Long itemId = item.getId();
        Long userId = user.getId();

        ItemResponseDto itemResponseDto = itemService.getById(itemId, userId);

        assertNotNull(itemResponseDto);
    }

    @Test
    public void testUpdate() {

        User user = new User();
        user.setEmail("test@example.com");
        user.setName("Test User");
        userRepository.save(user);

        Item item = new Item();
        item.setName("Test Item");
        item.setDescription("Test Description");
        item.setAvailable(true);
        item.setOwner(user);
        itemRepository.save(item);

        Long itemId = item.getId();
        Long userId = user.getId();

        Map<Object, Object> fields = new HashMap<>();
        fields.put("name", "Updated Item Name");

        ItemResponseDto itemResponseDto = itemService.update(itemId, fields, userId);

        assertNotNull(itemResponseDto);
    }

    @Test
    public void testDelete() {
        Item item = new Item();
        item.setName("Test Item");
        item.setDescription("Test Description");
        item.setAvailable(true);
        itemRepository.save(item);

        Long itemId = item.getId();

        itemService.delete(itemId);

        assertFalse(itemRepository.existsById(itemId));
    }

    @Test
    public void testSearch() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setName("Test User");
        userRepository.save(user);

        Item item1 = new Item();
        item1.setName("Test Item 1");
        item1.setDescription("Description item 1");
        item1.setAvailable(true);
        item1.setOwner(user);
        itemRepository.save(item1);

        Item item2 = new Item();
        item2.setName("Test Item 2");
        item2.setDescription("Description item 2");
        item2.setAvailable(true);
        item2.setOwner(user);
        itemRepository.save(item2);

        Item item3 = new Item();
        item3.setName("Another Item");
        item3.setDescription("Description item");
        item3.setAvailable(false);
        item3.setOwner(user);
        itemRepository.save(item3);

        List<ItemResponseDto> result = itemService.search("test");

        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(item -> item.getName().equals("Test Item 1")));
        assertTrue(result.stream().anyMatch(item -> item.getName().equals("Test Item 2")));
    }

    @Test
    public void testCreateComment() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setName("Test User");
        userRepository.save(user);

        User booker = new User();
        booker.setEmail("booker@booker.com");
        booker.setName("booker User");
        userRepository.save(booker);

        Item item = new Item();
        item.setName("Test Item");
        item.setDescription("Description item 1");
        item.setAvailable(true);
        item.setOwner(user);
        itemRepository.save(item);

        BookingDto bookingDto = new BookingDto();
        bookingDto.setItemId(item.getId());
        bookingDto.setStart(LocalDateTime.now());
        bookingDto.setEnd(LocalDateTime.now().plusDays(1));

        Booking booking = BookingMapper.toBooking(bookingDto, item, booker);

        bookingRepository.save(booking);

        CommentDto commentDto = new CommentDto();
        commentDto.setText("Test Comment");

        CommentResponseDto commentResponseDto = itemService.createComment(commentDto, booker.getId(), item.getId());

        assertNotNull(commentResponseDto.getId());
        assertEquals(commentDto.getText(), commentResponseDto.getText());
        assertEquals(booker.getName(), commentResponseDto.getAuthorName());
        assertEquals(item.getId(), commentResponseDto.getItem().getId());

        commentResponseDto.setItem(new Item());
        assertNotNull(commentResponseDto.getItem());
    }

    @Test
    public void testItemResponseDto() {
        Long id = 1L;
        String name = "Test Item";
        String description = "Test Description";
        User owner = new User(2L, "Test Owner");
        Boolean available = true;
        Long requestId = 3L;
        BookingDtoForItem lastBooking = new BookingDtoForItem();
        BookingDtoForItem nextBooking = new BookingDtoForItem();
        List<CommentResponseDto> comments = new ArrayList<>();

        ItemResponseDto itemResponseDto = new ItemResponseDto(id, name, description, owner, available,
                requestId, lastBooking, nextBooking, comments);

        assertEquals(id, itemResponseDto.getId());
        assertEquals(name, itemResponseDto.getName());
        assertEquals(description, itemResponseDto.getDescription());
        assertEquals(owner, itemResponseDto.getOwner());
        assertEquals(available, itemResponseDto.getAvailable());
        assertEquals(requestId, itemResponseDto.getRequestId());
        assertEquals(lastBooking, itemResponseDto.getLastBooking());
        assertEquals(nextBooking, itemResponseDto.getNextBooking());
        assertEquals(comments, itemResponseDto.getComments());
    }


    @Test
    public void testItemDtoShort() {
        Long id = 1L;
        String name = "Test Item";
        String description = "Test Description";
        Boolean available = true;
        Long requestId = 2L;

        ItemDtoShort itemDtoShort = new ItemDtoShort(id, name, description, available, requestId);

        assertEquals(id, itemDtoShort.getId());
        assertEquals(name, itemDtoShort.getName());
        assertEquals(description, itemDtoShort.getDescription());
        assertEquals(available, itemDtoShort.getAvailable());
        assertEquals(requestId, itemDtoShort.getRequestId());
    }

    @Test
    public void testToItemResponseDto() {
        Long itemId = 1L;
        String itemName = "Test Item";
        String itemDescription = "Test Description";
        Long ownerId = 2L;
        String ownerName = "Test Owner";
        Boolean itemAvailable = true;
        Long requestId = 3L;

        Item item = new Item();
        item.setId(itemId);
        item.setName(itemName);
        item.setDescription(itemDescription);
        User owner = new User();
        owner.setId(ownerId);
        owner.setName(ownerName);
        item.setOwner(owner);
        item.setAvailable(itemAvailable);
        item.setRequestId(requestId);

        List<Booking> bookings = new ArrayList<>();
        Booking booking1 = new Booking();
        booking1.setId(4L);
        booking1.setItem(item);
        booking1.setStatus(Status.APPROVED);
        booking1.setStart(LocalDateTime.now().minusDays(1));
        booking1.setEnd(LocalDateTime.now().plusDays(1));
        booking1.setBooker(new User(3L, "Ivan", "ivan@mail.ru"));
        bookings.add(booking1);
        Booking booking2 = new Booking();
        booking2.setId(5L);
        booking2.setItem(item);
        booking2.setStatus(Status.APPROVED);
        booking2.setStart(LocalDateTime.now().plusDays(2));
        booking2.setEnd(LocalDateTime.now().plusDays(3));
        booking2.setBooker(new User(3L, "Petr", "petr@mail.ru"));
        bookings.add(booking2);

        List<CommentResponseDto> comments = new ArrayList<>();

        User ownerDto = new User(ownerId, ownerName);

        BookingDtoForItem bookingLastDto = new BookingDtoForItem();
        bookingLastDto.setId(booking1.getId());

        BookingDtoForItem bookingNextDto = new BookingDtoForItem();
        bookingNextDto.setId(booking2.getId());

        ItemResponseDto expectedDto = new ItemResponseDto();
        expectedDto.setId(itemId);
        expectedDto.setName(itemName);
        expectedDto.setDescription(itemDescription);
        expectedDto.setOwner(ownerDto);
        expectedDto.setAvailable(itemAvailable);
        expectedDto.setLastBooking(bookingLastDto);
        expectedDto.setNextBooking(bookingNextDto);
        expectedDto.setComments(comments);
        expectedDto.setRequestId(requestId);

        ItemResponseDto actualDto = ItemMapper.toItemResponseDto(item, bookings, comments);

        assertEquals(expectedDto.getId(), actualDto.getId());
        assertEquals(expectedDto.getName(), actualDto.getName());
        assertEquals(expectedDto.getDescription(), actualDto.getDescription());
    }
}
