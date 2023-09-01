package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ReflectionUtils;
import ru.practicum.shareit.exceptions.ValidationIdException;
import ru.practicum.shareit.user.dto.UserDto;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Класс описывает UserService, с основной логикой
 */

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public UserDto create(UserDto dto) {
        User user = UserMapper.toUser(dto);
        User newUser = userRepository.save(user);
        return UserMapper.toUserDto(newUser);
    }

    public List<UserDto> getAll() {
        List<User> userList = userRepository.findAll();
        return userList.stream().map(UserMapper::toUserDto).collect(Collectors.toList());
    }

    public UserDto getById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new ValidationIdException("Пользователь не найден"));
        return UserMapper.toUserDto(user);
    }

    @Transactional
    public UserDto update(Long id, Map<Object, Object> fields) {
        User user = userRepository.findById(id).orElseThrow(() -> new ValidationIdException("Пользователь не найден"));
        fields.forEach((key, value) -> {
            if (!key.equals("id")) {
                Field field = ReflectionUtils.findField(User.class, (String) key);
                assert field != null;
                field.setAccessible(true);
                ReflectionUtils.setField(field, user, value);
            }
        });
        User newUser = userRepository.save(user);
        return UserMapper.toUserDto(newUser);
    }

    public void delete(Long id) {
        userRepository.deleteById(id);
    }

}
