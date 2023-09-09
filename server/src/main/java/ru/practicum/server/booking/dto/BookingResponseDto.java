package ru.practicum.server.booking.dto;

import lombok.*;
import ru.practicum.server.booking.Status;
import ru.practicum.server.item.Item;
import ru.practicum.server.user.User;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingResponseDto {

    private Long id;
    private Item item;
    private LocalDateTime start;
    private LocalDateTime end;
    private User booker;
    private Status status;
}