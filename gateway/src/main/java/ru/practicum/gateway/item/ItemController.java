package ru.practicum.gateway.item;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.gateway.item.dto.CommentDto;
import ru.practicum.gateway.item.dto.ItemDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.Map;

@RestController
@RequestMapping("/items")
@Slf4j
@AllArgsConstructor
@Validated
public class ItemController {

    private final ItemClient itemClient;
    private static final String REQUEST_HEADER = "X-Sharer-User-Id";

    @PostMapping
    public ResponseEntity<Object> create(@RequestHeader(REQUEST_HEADER) Long userId,
                                         @RequestBody @Valid ItemDto dto,
                                         BindingResult result) {
        log.info("Получен запрос к эндпоинту /items create с headers {}", userId);
        return itemClient.create(dto, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getAll(@RequestHeader(REQUEST_HEADER) Long userId) {
        log.info("Получен запрос к эндпоинту: /items getAll с headers {}", userId);
        return itemClient.getAll(userId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getById(@RequestHeader(REQUEST_HEADER) @Positive Long userId,
                                          @PathVariable("id") @Positive Long itemId) {
        log.info("Получен запрос к эндпоинту: /items geById с id={}", itemId);
        return itemClient.getById(itemId, userId);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> update(@RequestHeader(REQUEST_HEADER) Long userId,
                                         @PathVariable("id") Long itemId,
                                         @RequestBody Map<Object, Object> fields,
                                         BindingResult result) {
        log.info("Получен запрос к эндпоинту: /items update с ItemId={} с headers {}", itemId, userId);
        return itemClient.update(itemId, userId, fields);
    }

    @DeleteMapping("/{id}")
    public HttpStatus delete(@PathVariable("id") @Positive Long itemId) {
        log.info("Получен запрос к эндпоинту: /items delete с id={}", itemId);
        itemClient.delete(itemId);
        return HttpStatus.OK;
    }

    @GetMapping("/search")
    public ResponseEntity<Object> search(@RequestParam("text") String text) {
        log.info("Получен запрос к эндпоинту: items/search с text: {}", text);
        return itemClient.search(text);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addComment(@RequestHeader(REQUEST_HEADER) Long userId,
                                             @PathVariable("itemId") @Positive Long itemId,
                                             @Valid @RequestBody CommentDto comment,
                                             BindingResult result) {
        log.info("Получен запрос к эндпоинту /items{itemId}/comment addComment с headers {}, с itemId {}", userId, itemId);
        return itemClient.createComment(itemId, userId, comment);
    }
}