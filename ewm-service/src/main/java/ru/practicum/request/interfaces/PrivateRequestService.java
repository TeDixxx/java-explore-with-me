package ru.practicum.request.interfaces;

import ru.practicum.request.dto.ParticipationRequestDto;

import java.util.List;

public interface PrivateRequestService {

    ParticipationRequestDto createRequest(Long userId, Long eventId);

    List<ParticipationRequestDto> getRequest(Long userId);

    ParticipationRequestDto cancelRequest(Long userId, Long requestId);
}
