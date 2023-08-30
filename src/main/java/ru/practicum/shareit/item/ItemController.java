package ru.practicum.shareit.item;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.Valid;

import java.util.List;

/**
 * Класс описывает ItemController с следующими энпоинтами
 * - GET /items/{id} -  получать данные вещи по идентификатору
 * - GET /items/ -  получать данные всех вещей
 * - POST /items/ -  добавлять вещь в память
 * - PATCH /items/{id} - обновление вещи по id
 * - DELETE  /items/{id} - удаление вещи по id
 */
@RestController
@RequestMapping("/items")
@Slf4j
@AllArgsConstructor
public class ItemController {
    private final ItemService itemService;
    private final String requestHeader = "X-Sharer-User-Id";

    @PostMapping
    public ItemDto create(@RequestHeader(name = requestHeader) Long userId,
                          @Valid @RequestBody ItemDto dto) {
        log.info("Получен запрос к эндпоинту /items create с headers {}", userId);
        return itemService.create(dto, userId);
    }

    @GetMapping
    public List<ItemDto> getAll(@RequestHeader(name = requestHeader) Long userId) {
        log.info("Получен запрос к эндпоинту: /items getAll с headers {}", userId);
        return itemService.getAll(userId);
    }

    @GetMapping("/{id}")
    public ItemDto getById(@PathVariable("id") Long itemId) {
        log.info("Получен запрос к эндпоинту: /items geById с id={}", itemId);
        return itemService.getById(itemId);
    }

    @PatchMapping("/{id}")
    public ItemDto update(@RequestHeader(name = requestHeader) Long userId,
                          @PathVariable("id") Long itemId,
                          @RequestBody ItemDto dto) {
        log.info("Получен запрос к эндпоинту: /items update с ItemId={} с headers {}", itemId, userId);
        return itemService.update(itemId, dto, userId);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long userId) {
        log.info("Получен запрос к эндпоинту: /items delete с id={}", userId);
        itemService.delete(userId);
    }

    @GetMapping("/search")
    public List<ItemDto> search(@RequestParam("text") String text) {
        log.info("Получен запрос к эндпоинту: items/search с text: {}", text);
        return itemService.search(text);
    }
}
