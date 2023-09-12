package ru.practicum.server.booking;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.server.booking.dto.BookingDto;
import ru.practicum.server.booking.dto.BookingResponseDto;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/bookings")
@Validated
@AllArgsConstructor
public class BookingController {

    private final BookingService bookingService;
    private static final String REQUEST_HEADER = "X-Sharer-User-Id";

    @PostMapping
    public BookingResponseDto addReservation(@RequestHeader(REQUEST_HEADER) Long userId,
                                             @RequestBody BookingDto dto,
                                             BindingResult result) {
        log.info("Получен запрос к эндпоинту /bookings addReservation с headers {}", userId);
        return bookingService.create(dto, userId);
    }

    @PatchMapping("/{bookingId}")
    public BookingResponseDto updateStatus(@RequestHeader(REQUEST_HEADER) Long userId,
                                           @PathVariable("bookingId") Long bookingId,
                                           @RequestParam("approved") Boolean approved) {
        log.info("Получен запрос к эндпоинту /bookings updateStatus с headers {}, с bookingId {}, статус {}",
                userId, bookingId, approved);
        return bookingService.setApproved(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingResponseDto getById(@RequestHeader(REQUEST_HEADER) Long userId,
                                      @PathVariable("bookingId") Long bookingId) {
        log.info("Получен запрос к эндпоинту /bookings getById с headers {}, с bookingId {}", userId, bookingId);
        return bookingService.getById(userId, bookingId);
    }

    @GetMapping
    public List<BookingResponseDto> getAllReservation(@RequestHeader(REQUEST_HEADER) Long userId,
                                                      @RequestParam(value = "state", defaultValue = "ALL") State state,
                                                      @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                      @RequestParam(name = "size", defaultValue = "10") Integer size) throws Throwable {
        log.info("Получен запрос к эндпоинту /bookings getAllReservation с state {}", state);
        return bookingService.getAllReserve(userId, state, "booker", from, size);
    }

    @GetMapping("/owner")
    public List<BookingResponseDto> getReservationForOwner(@RequestHeader(REQUEST_HEADER) Long userId,
                                                           @RequestParam(value = "state", defaultValue = "ALL") State state,
                                                           @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                           @RequestParam(name = "size", defaultValue = "10") Integer size) throws Throwable {
        log.info("Получен запрос к эндпоинту /bookings getAllReservation с state {}", state);
        return bookingService.getAllReserve(userId, state, "owner", from, size);
    }
}