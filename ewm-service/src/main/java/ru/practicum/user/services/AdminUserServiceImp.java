package ru.practicum.user.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import org.springframework.stereotype.Service;

import ru.practicum.exceptions.NotFoundException;
import ru.practicum.user.dto.NewUserRequest;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.interfaces.UserRepository;
import ru.practicum.user.interfaces.AdminUserService;

import ru.practicum.user.model.UserMapper;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminUserServiceImp implements AdminUserService {


    private final UserRepository repository;

    @Override
    public UserDto createUser(NewUserRequest userRequest) {
        return UserMapper.toUserDto(repository.save(UserMapper.toUser(userRequest)));
    }

    @Override
    public List<UserDto> getUsers(List<Long> ids, Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from, size);
        if (ids == null) {
            return repository.findAll(pageable)
                    .stream()
                    .map(UserMapper::toUserDto)
                    .collect(Collectors.toList());
        } else {
            return repository.findByIds(ids, pageable)
                    .stream()
                    .map(UserMapper::toUserDto)
                    .collect(Collectors.toList());
        }


    }

    @Override
    public void deleteUser(Long userId) {
        repository.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));
        repository.deleteById(userId);
    }
}
