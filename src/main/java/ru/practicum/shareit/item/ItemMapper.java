package ru.practicum.shareit.item;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.dto.BookingDtoForItem;
import ru.practicum.shareit.item.dto.CommentResponseDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoShort;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@UtilityClass
public class ItemMapper {
    public ItemDtoShort toItemDtoShort(Item item) {
        return ItemDtoShort
                .builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .requestId(item.getRequestId())
                .build();
    }

    public Item toItem(ItemDto dto, User user) {
        return Item.builder()
                .id(dto.getId())
                .name(dto.getName())
                .description(dto.getDescription())
                .available(dto.getAvailable())
                .owner(user)
                .requestId(dto.getRequestId())
                .build();
    }

    public ItemResponseDto toItemResponseDto(Item item, List<Booking> booking, List<CommentResponseDto> comment) {
        BookingDtoForItem bookingLast = null;
        BookingDtoForItem bookingNext = null;
        LocalDateTime time = LocalDateTime.now();

        if (!booking.isEmpty()) {

            Optional<Booking> bookingLastOld = booking.stream()
                    .filter(b -> (b.getItem().getId().equals(item.getId()) && b.getStatus().equals(Status.APPROVED)))
                    .filter(b -> (b.getStart().isBefore(time) && b.getEnd().isAfter(time)) || b.getEnd().isBefore(time))
                    .sorted(Comparator.comparing(Booking::getId).reversed())
                    .findFirst();

            Optional<Booking> bookingNextOld = booking.stream()
                    .filter(b -> b.getItem().getId().equals(item.getId()) && b.getStatus().equals(Status.APPROVED))
                    .sorted(Comparator.comparing(Booking::getStart))
                    .filter(b -> b.getStart().isAfter(time))
                    .findFirst();
            if (bookingLastOld.isPresent()) {
                bookingLast = BookingMapper.toBookingDtoForItem(bookingLastOld.get());
            }
            if (bookingNextOld.isPresent()) {
                bookingNext = BookingMapper.toBookingDtoForItem(bookingNextOld.get());
            }

        }
        return ItemResponseDto
                .builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .owner(new User(item.getOwner().getId(), item.getOwner().getName()))
                .available(item.getAvailable())
                .lastBooking(bookingLast)
                .nextBooking(bookingNext)
                .comments(comment)
                .requestId(item.getRequestId())
                .build();
    }
}
