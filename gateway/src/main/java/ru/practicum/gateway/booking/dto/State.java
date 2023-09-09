package ru.practicum.gateway.booking.dto;

import java.util.Optional;

public enum State {
    ALL, WAITING, CURRENT, PAST, REJECTED, FUTURE;

    public static Optional<State> from(String stringState) {
        for (State state : values()) {
            if (state.name().equalsIgnoreCase(stringState)) {
                return Optional.of(state);
            }
        }
        return Optional.empty();
    }
}