package ru.practicum.server.user;

import lombok.experimental.UtilityClass;
import ru.practicum.server.user.dto.UserDto;

@UtilityClass
public class UserMapper {
    public UserDto toUserDto(User newUser) {
        return UserDto.builder()
                .id(newUser.getId())
                .name(newUser.getName())
                .email(newUser.getEmail())
                .build();
    }

    public User toUser(UserDto dto) {
        return User.builder()
                .id(dto.getId())
                .name(dto.getName())
                .email(dto.getEmail())
                .build();
    }
}