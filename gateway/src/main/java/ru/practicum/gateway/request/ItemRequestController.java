package ru.practicum.gateway.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.gateway.request.dto.ItemRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@RestController
@RequestMapping(path = "/requests")
@Validated
@Slf4j
@RequiredArgsConstructor
public class ItemRequestController {

    private final ItemRequestClient requestClient;
    private static final String REQUEST_HEADER = "X-Sharer-User-Id";

    @PostMapping
    public ResponseEntity<Object> create(@RequestHeader(REQUEST_HEADER) @Positive Long userId,
                                         @Valid @RequestBody ItemRequestDto itemRequestDto,
                                         BindingResult result) {
        log.info("Получен запрос к эндпоинту /requests create с headers {}", userId);
        return requestClient.create(userId, itemRequestDto);
    }

    @GetMapping
    public ResponseEntity<Object> getForUser(@RequestHeader(REQUEST_HEADER) Long userId) {
        log.info("Получен запрос к эндпоинту /requests getForUser с headers {}", userId);
        return requestClient.getForUser(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getOtherUsers(@RequestHeader(REQUEST_HEADER) Long userId,
                                                @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero Integer from,
                                                @RequestParam(name = "size", defaultValue = "10") @Positive Integer size) {
        log.info("Получен запрос к эндпоинту /requests getOtherUsers с headers {}, from{}, size{}", userId, from, size);
        return requestClient.getOtherUsers(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getRequestById(@RequestHeader(REQUEST_HEADER) Long userId,
                                                 @PathVariable(name = "requestId") Long requestId) {
        log.info("Получен запрос к эндпоинту /requests getOtherUsers с headers {}, c requestId {}", userId, requestId);
        return requestClient.getRequestById(userId, requestId);
    }
}