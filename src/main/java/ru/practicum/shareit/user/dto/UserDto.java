package ru.practicum.shareit.user.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.user.Create;
import ru.practicum.shareit.user.Update;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * Класс описывает модель UserDto
 */
@Data
@Builder
public class UserDto {
    private Long id;
    @NotBlank(groups = {Create.class}, message = "Имя не может быть пустым")
    private String name;
    @NotBlank(groups = {Create.class}, message = "Email не может быть пустым")
    @Email(groups = {Create.class, Update.class}, message = "Электронная почта не может быть пустой и должна содержать символ @")
    private String email;
}
