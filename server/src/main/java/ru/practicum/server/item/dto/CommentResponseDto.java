package ru.practicum.server.item.dto;

import lombok.*;

import java.time.LocalDateTime;
import ru.practicum.server.item.Item;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentResponseDto {
    private Long id;
    private String text;
    private Item item;
    private String authorName;
    private LocalDateTime created;
}