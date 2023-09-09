package ru.practicum.server.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.server.request.dto.ItemRequestResponseDto;
import ru.practicum.server.request.dto.ItemRequestDto;

import java.util.List;

@RestController
@RequestMapping(path = "/requests")
@Validated
@Slf4j
@RequiredArgsConstructor
public class ItemRequestController {

    private final ItemRequestService requestService;
    private static final String REQUEST_HEADER = "X-Sharer-User-Id";

    @PostMapping
    public ItemRequestResponseDto create(@RequestHeader(REQUEST_HEADER) Long userId,
                                         @RequestBody ItemRequestDto dto,
                                         BindingResult result) {
        log.info("Получен запрос к эндпоинту /requests create с headers {}", userId);
        return requestService.create(dto, userId);
    }

    @GetMapping
    public List<ItemRequestResponseDto> getForUser(@RequestHeader(REQUEST_HEADER) Long userId) {
        log.info("Получен запрос к эндпоинту /requests getForUser с headers {}", userId);
        return requestService.getForUser(userId);
    }

    @GetMapping("/all")
    public List<ItemRequestResponseDto> getOtherUsers(@RequestHeader(REQUEST_HEADER) Long userId,
                                                      @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                      @RequestParam(name = "size", defaultValue = "10")Integer size) {
        log.info("Получен запрос к эндпоинту /requests getOtherUsers с headers {}, from{}, size{}", userId, from, size);
        return requestService.getOtherUsers(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public ItemRequestResponseDto getRequestById(@RequestHeader(REQUEST_HEADER) Long userId,
                                                 @PathVariable(name = "requestId") Long requestId) {
        log.info("Получен запрос к эндпоинту /requests getOtherUsers с headers {}, c requestId {}", userId, requestId);
        return requestService.getRequestById(userId, requestId);
    }
}