package ru.practicum.event.services;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import org.springframework.web.server.ResponseStatusException;
import ru.practicum.category.interfaces.CategoryRepository;
import ru.practicum.category.model.Category;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.dto.UpdateEventUserRequest;
import ru.practicum.event.enums.State;
import ru.practicum.event.enums.StateAction;
import ru.practicum.event.interfaces.EventRepository;
import ru.practicum.event.interfaces.PrivateEventService;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.EventMapper;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.request.dto.EventRequestStatusUpdateRequest;
import ru.practicum.request.dto.EventRequestStatusUpdateResult;
import ru.practicum.request.dto.ParticipationRequestDto;
import ru.practicum.request.interfaces.RequestRepository;
import ru.practicum.request.model.Request;
import ru.practicum.request.model.RequestMapper;
import ru.practicum.request.model.RequestStatus;

import ru.practicum.user.interfaces.UserRepository;
import ru.practicum.user.model.User;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PrivateEventServiceImpl implements PrivateEventService {

    private final EventRepository eventRepository;

    private final CategoryRepository categoryRepository;

    private final RequestRepository requestRepository;

    private final UserRepository userRepository;


    @Override
    public EventFullDto addNewEvent(Long userId, NewEventDto dto) {

        if (dto.getEventDate().isBefore(LocalDateTime.now())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Date can't be before");
        }

        if (dto.getEventDate().isBefore(LocalDateTime.now().plusHours(2L))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        Category category = categoryRepository.findById(dto.getCategory()).orElseThrow(()
                -> new NotFoundException("Category not found"));

        User initiator = userRepository.findById(userId).orElseThrow(()
                -> new NotFoundException("User not found"));

        return EventMapper.toFullDto(eventRepository.save(EventMapper.toEvent(dto, category, initiator)));
    }

    @Override
    public List<EventShortDto> getEventsByUser(Long userId, Integer from, Integer size) {
        User user = userRepository.findById(userId).orElseThrow(()
                -> new NotFoundException("User not found"));

        Pageable pageable = PageRequest.of(from, size);

        List<Event> events = eventRepository.findAllByInitiatorId(user.getId(), pageable);

        return events.stream().map(EventMapper::toShortDto).peek(e
                -> e.setConfirmedRequests(requestRepository.countByEventIdAndStatus(e.getId(),
                RequestStatus.CONFIRMED))).collect(Collectors.toList());
    }

    @Override
    public EventFullDto getUserEvent(Long userId, Long eventId) {
        User user = userRepository.findById(userId).orElseThrow(()
                -> new NotFoundException("User not found"));

        eventRepository.findById(eventId).orElseThrow(()
                -> new NotFoundException("Event not found"));

        Event event = eventRepository.findByInitiatorIdAndId(userId, eventId);

        return EventMapper.toFullDto(event);
    }

    @Override
    public EventFullDto update(Long userId, Long eventId, UpdateEventUserRequest request) {

        User user = userRepository.findById(userId).orElseThrow(()
                -> new NotFoundException("User not found"));

        eventRepository.findById(eventId).orElseThrow(()
                -> new NotFoundException("Event not found"));

        Event event = eventRepository.findByInitiatorIdAndId(user.getId(), eventId);

        if (request.getEventDate() != null) {
            if (request.getEventDate().isBefore(LocalDateTime.now().plusHours(2L)) && request.getEventDate() != null) {
                throw new ResponseStatusException(HttpStatus.CONFLICT);
            }
        }

        if (event.getState().equals(State.PUBLISHED)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }


        if (request.getAnnotation() != null) {
            event.setAnnotation(request.getAnnotation());
        }

        if (request.getDescription() != null) {
            event.setDescription(request.getDescription());
        }

        if (request.getEventDate() != null) {
            event.setEventDate(request.getEventDate());
        }

        if (request.getRequestModeration() != null) {
            event.setRequestModeration(request.getRequestModeration());
        }

        if (request.getLocation() != null) {
            event.setLocation(request.getLocation());
        }

        if (request.getPaid() != null) {
            event.setPaid(request.getPaid());
        }

        if (request.getParticipantLimit() != null) {
            event.setParticipantLimit(request.getParticipantLimit());
        }

        if (request.getTitle() != null) {
            event.setTitle(request.getTitle());
        }

        if (request.getStateAction().equals(StateAction.SEND_TO_REVIEW)) {
            event.setState(State.PENDING);
        }

        if (request.getStateAction().equals(StateAction.CANCEL_REVIEW)) {
            event.setState(State.CANCELED);
        }

        if (request.getCategory() != null) {
            Category category = categoryRepository.findById(request.getCategory()).orElseThrow(()
                    -> new NotFoundException("Category not found"));
            event.setCategory(category);
        }


        return EventMapper.toFullDto(event);
    }

    //???
    @Override
    public EventRequestStatusUpdateResult updateRequestStatus(Long userId, Long eventId, EventRequestStatusUpdateRequest request) {
        User initiator = userRepository.findById(userId).orElseThrow(()
                -> new NotFoundException("User not found"));

        Event event = eventRepository.findById(eventId).orElseThrow(()
                -> new NotFoundException("Event not found"));

        if (!event.getInitiator().equals(initiator)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Only initiator can accept");
        }

        List<Request> requests = requestRepository.findByIdIn(request.getRequestIds());

        Long counter = requestRepository.countByEventId(event.getId());

        if (event.getParticipantLimit() <= counter) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }

        List<ParticipationRequestDto> confirmed = new ArrayList<>();
        List<ParticipationRequestDto> rejected = new ArrayList<>();

            if (RequestStatus.CONFIRMED.equals(request.getStatus()) && requests.size() > 0) {
                int min = 0;
                int max;

                if (event.getParticipantLimit() <= requests.size()) {
                    max = Math.toIntExact(event.getParticipantLimit());
                } else {
                    max = requests.size();
                }

                for (int i = 0; i < max; i++) {
                    requests.get(i).setStatus(RequestStatus.CONFIRMED);
                    requestRepository.save(requests.get(i));
                    min++;
                    confirmed.add(RequestMapper.toDto(requests.get(i)));
                }

                for(int i = min; i < requests.size(); i++){

                    if (requests.get(i).getStatus().equals(RequestStatus.CONFIRMED)) {
                        throw new ResponseStatusException(HttpStatus.CONFLICT);
                    }

                    requests.get(i).setStatus(RequestStatus.REJECTED);
                    requestRepository.save(requests.get(i));
                    rejected.add(RequestMapper.toDto(requests.get(i)));

                }

            } else {
                for (int i = 0; i < requests.size(); i++) {

                    if (requests.get(i).getStatus().equals(RequestStatus.CONFIRMED)) {
                        throw new ResponseStatusException(HttpStatus.CONFLICT);
                    }
                    requests.get(i).setStatus(RequestStatus.REJECTED);
                    requestRepository.save(requests.get(i));
                    rejected.add(RequestMapper.toDto(requests.get(i)));

                }
            }

        return new EventRequestStatusUpdateResult(confirmed, rejected);

    }

    @Override
    public List<ParticipationRequestDto> getParticipationRequests(Long userId, Long eventId) {

        User user = userRepository.findById(userId).orElseThrow(()
                -> new NotFoundException("User not found"));

        Event event = eventRepository.findById(eventId).orElseThrow(()
                -> new NotFoundException("Event not found"));

        if (!event.getInitiator().equals(user)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        List<Request> requests = requestRepository.findAllByEventId(eventId);

        return requests.stream()
                .map(RequestMapper::toDto)
                .collect(Collectors.toList());
    }

}

