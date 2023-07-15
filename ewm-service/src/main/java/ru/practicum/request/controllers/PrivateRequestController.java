package ru.practicum.request.controllers;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.request.dto.ParticipationRequestDto;
import ru.practicum.request.interfaces.PrivateRequestService;

import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users/{userId}/requests")
public class PrivateRequestController {

    private final PrivateRequestService service;


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestDto createRequest(@PathVariable @NotNull Long userId, @RequestParam Long eventId) {
        return service.createRequest(userId, eventId);
    }

    @GetMapping
    public List<ParticipationRequestDto> getRequest(@NotNull @PathVariable Long userId) {
        return service.getRequest(userId);
    }

    @PatchMapping("/{requestId}/cancel")
    public ParticipationRequestDto cancelRequest(@PathVariable @NotNull Long userId,
                                                 @PathVariable @NotNull Long requestId) {
        return service.cancelRequest(userId, requestId);
    }
}
