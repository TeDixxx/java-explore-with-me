package ru.practicum.request.model;

import ru.practicum.request.dto.ParticipationRequestDto;

public class RequestMapper {

    public static ParticipationRequestDto toDto(Request request) {
        return ParticipationRequestDto.builder()
                .id(request.getId())
                .created(request.getEvent().getCreatedOn())
                .event(request.getEvent().getId())
                .requester(request.getRequester().getId())
                .status(request.getStatus())
                .build();
    }
}
