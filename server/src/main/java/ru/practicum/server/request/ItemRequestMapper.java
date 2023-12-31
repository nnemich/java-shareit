package ru.practicum.server.request;

import lombok.experimental.UtilityClass;
import ru.practicum.server.item.dto.ItemDtoShort;
import ru.practicum.server.request.dto.ItemRequestDto;
import ru.practicum.server.request.dto.ItemRequestResponseDto;

import java.time.LocalDateTime;
import java.util.List;

@UtilityClass
public class ItemRequestMapper {
    public ItemRequest toItemRequest(ItemRequestDto dto, Long userId) {
        return ItemRequest.builder()
                .description(dto.getDescription())
                .created(LocalDateTime.now())
                .requestor(userId)
                .build();
    }

    public ItemRequestResponseDto toItemRequestResponseDto(ItemRequest itemRequest, List<ItemDtoShort> items) {
        return ItemRequestResponseDto.builder()
                .id(itemRequest.getId())
                .description(itemRequest.getDescription())
                .created(itemRequest.getCreated())
                .items(items)
                .build();
    }
}