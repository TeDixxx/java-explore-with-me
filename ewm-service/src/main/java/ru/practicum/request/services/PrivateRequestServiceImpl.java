package ru.practicum.request.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.event.enums.State;
import ru.practicum.event.interfaces.EventRepository;
import ru.practicum.event.model.Event;
import ru.practicum.exceptions.ConflictException;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.request.dto.ParticipationRequestDto;
import ru.practicum.request.interfaces.PrivateRequestService;
import ru.practicum.request.interfaces.RequestRepository;
import ru.practicum.request.model.Request;
import ru.practicum.request.model.RequestMapper;
import ru.practicum.request.model.RequestStatus;
import ru.practicum.user.interfaces.UserRepository;
import ru.practicum.user.model.User;


import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PrivateRequestServiceImpl implements PrivateRequestService {


    private final RequestRepository requestRepository;

    private final UserRepository userRepository;

    private final EventRepository eventRepository;


    @Override
    public ParticipationRequestDto createRequest(Long userId, Long eventId) {

        User requester = userRepository.findById(userId).orElseThrow(()
                -> new NotFoundException("User not found"));

        Event event = eventRepository.findById(eventId).orElseThrow(()
                -> new NotFoundException("Event not found вот тут"));

        if (event.getInitiator().getId().equals(userId)) {
            throw new ConflictException("Инициатор события не может добавить запрос на участие в своём событии");
        }

        if (event.getState().equals(State.PENDING) || event.getState().equals(State.CANCELED)) {
            throw new ConflictException("Нельзя участвовать в неопубликованном событии");
        }

        Long countRequest = requestRepository.countByEventId(eventId);
        if (event.getParticipantLimit() > 0 && event.getParticipantLimit() <= countRequest) {
            throw new ConflictException("Достигнут лимит запросов на участие");
        }

        Request request;

        if (event.getRequestModeration()) {
            request = Request.builder()
                    .created(LocalDateTime.now())
                    .requester(requester)
                    .event(event)
                    .status(RequestStatus.PENDING)
                    .build();

        } else {
            request = Request.builder()
                    .created(LocalDateTime.now())
                    .requester(requester)
                    .event(event)
                    .status(RequestStatus.CONFIRMED)
                    .build();
        }

        if (event.getParticipantLimit() == 0) {
            request.setStatus(RequestStatus.CONFIRMED);
        }

        if (request.getStatus() == RequestStatus.CONFIRMED && event.getConfirmedRequests() != null) {
            event.setConfirmedRequests(event.getConfirmedRequests() + 1);
            eventRepository.save(event);
        }


        return RequestMapper.toDto(requestRepository.save(request));

    }

    @Override
    public List<ParticipationRequestDto> getRequest(Long userId) {

        User requester = userRepository.findById(userId).orElseThrow(()
                -> new NotFoundException("User not found"));

        return requestRepository.findByRequester(requester).stream()
                .map(RequestMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public ParticipationRequestDto cancelRequest(Long userId, Long requestId) {

        userRepository.findById(userId).orElseThrow(()
                -> new NotFoundException("User not found"));

        Request request = requestRepository.findById(requestId).orElseThrow(()
                -> new NotFoundException("Request not found"));

        request.setStatus(RequestStatus.CANCELED);

        return RequestMapper.toDto(requestRepository.save(request));
    }


}
