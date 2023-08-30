package ru.practicum.shareit.user;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.Valid;
import java.util.List;

/**
 *  * Класс описывает UserController с следующими энпоинтами
 *  * - GET /users/{id} -  получать пользователя по идентификатору
 *  * - GET /users/ -  получать всех пользователей
 *  * - POST /users/ -  добавлять пользователя в память
 *  * - PATCH /users/{id} - обновление пользователя по id
 *  * - DELETE  /users/{id} - удаление пользователя по id
 */
@RestController
@RequestMapping(path = "/users")
@Validated
@Slf4j
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public UserDto create(@Valid @RequestBody UserDto user) {
        log.info("Получен запрос к эндпоинту /users create");
        return userService.create(user);
    }

    @GetMapping
    public List<UserDto> getAll() {
        log.info("Получен запрос к эндпоинту: /users getAll");
        return userService.getAll();
    }

    @GetMapping("/{id}")
    public UserDto getById(@PathVariable("id") Long userId) {
        log.info("Получен запрос к эндпоинту: /users geById с id={}", userId);
        return userService.getById(userId);
    }

    @PatchMapping("/{id}")
    public UserDto update(@PathVariable("id") Long userId,
                          @RequestBody UserDto dto) {
        log.info("Получен запрос к эндпоинту: /users update с id={}", userId);
        return userService.update(userId, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long userId) {
        log.info("Получен запрос к эндпоинту: /users delete с id={}", userId);
        userService.delete(userId);
    }
}
