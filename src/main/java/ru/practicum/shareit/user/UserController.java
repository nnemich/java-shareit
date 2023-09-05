package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;
import java.util.Map;

/**
 * Класс описывает UserController с следующими энпоинтами
 * - GET /users/{id} -  получать пользователя по идентификатору
 * - GET /users/ -  получать всех пользователей
 * - POST /users/ -  добавлять пользователя в память
 * - PATCH /users/{id} - обновление пользователя по id
 * - DELETE  /users/{id} - удаление пользователя по id
 */
@RestController
@RequestMapping(path = "/users")
@Validated
@Slf4j
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public UserDto create(@Valid @RequestBody UserDto user, BindingResult result) {
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
                          @RequestBody Map<Object, Object> fields) {
        log.info("Получен запрос к эндпоинту: /users update с id={}", userId);
        return userService.update(userId, fields);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") @Positive Long userId) {
        log.info("Получен запрос к эндпоинту: /users delete с id={}", userId);
        userService.delete(userId);
    }
}
