package ru.practicum.user.interfaces;

import ru.practicum.user.dto.NewUserRequest;
import ru.practicum.user.dto.UserDto;


import java.util.List;

public interface AdminUserService {

    UserDto createUser(NewUserRequest userRequest);

    List<UserDto> getUsers(List<Long> ids, Integer from, Integer size);

    void deleteUser(Long userId);
}
