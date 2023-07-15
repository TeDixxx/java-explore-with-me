package ru.practicum.event.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.StatsClient;
import ru.practicum.category.interfaces.CategoryRepository;
import ru.practicum.category.model.Category;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.UpdateEventAdminRequest;
import ru.practicum.event.enums.State;
import ru.practicum.event.enums.StateAction;
import ru.practicum.event.interfaces.AdminEventService;
import ru.practicum.event.interfaces.EventRepository;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.EventMapper;
import ru.practicum.exceptions.BadRequestException;
import ru.practicum.exceptions.ConflictException;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.model.ViewStatsDto;
import ru.practicum.request.interfaces.RequestRepository;
import ru.practicum.request.model.RequestStatus;
import ru.practicum.user.interfaces.UserRepository;
import ru.practicum.user.model.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class AdminEventServiceImpl implements AdminEventService {

    private final EventRepository eventRepository;

    private final UserRepository userRepository;

    private final CategoryRepository categoryRepository;

    private final RequestRepository requestRepository;

    private final StatsClient client;


    @Override
    public List<EventFullDto> findEvents(List<Long> users, List<State> states, List<Long> categories, String
            rangeStart, String rangeEnd, Integer from, Integer size) {

        List<State> eventState;
        if (states != null) {
            eventState = new ArrayList<>(states);
        } else {
            eventState = Arrays.asList(State.values());
        }

        LocalDateTime start = LocalDateTime.now().minusYears(1L);
        LocalDateTime end = LocalDateTime.now().plusYears(10L);

        if (rangeStart != null) {
            start = LocalDateTime.parse(rangeStart, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        }
        if (rangeEnd != null) {
            end = LocalDateTime.parse(rangeEnd, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        }


        if (users == null || users.size() == 0) {
            users = userRepository.findAll().stream()
                    .map(User::getId)
                    .collect(Collectors.toList());
        }

        if (categories == null || categories.size() == 0) {
            categories = categoryRepository.findAll().stream()
                    .map(Category::getId)
                    .collect(Collectors.toList());
        }

        Pageable pageable = PageRequest.of(from, size);

        List<Event> events = eventRepository.findAllByInitiatorIdInAndStateInAndCategoryIdInAndEventDateIsAfterAndEventDateIsBefore(users, eventState, categories, start, end, pageable);


        return events.stream()
                .map(EventMapper::toFullDto)
                .peek(e -> e.setConfirmedRequests(requestRepository.countByEventIdAndStatus(e.getId(), RequestStatus.CONFIRMED)))
                // .peek(e -> e.setViews(getViews(rangeStart, rangeEnd, "/events/" + e.getId(), false)))
                .collect(Collectors.toList());
    }

    @Override
    public EventFullDto update(Long eventId, UpdateEventAdminRequest dto) {

        Event event = eventRepository.findById(eventId).orElseThrow(()
                -> new NotFoundException("Event not found"));

        if (dto.getEventDate() != null) {
            if (dto.getEventDate().isBefore(LocalDateTime.now().plusHours(1L))) {
                throw new BadRequestException("CONFLICT");
            }
        }

        if (event.getState().equals(State.PUBLISHED) || event.getState().equals(State.CANCELED)) {
            throw new ConflictException("Only pending or canceled events can be changed");
        }


        if (dto.getAnnotation() != null) {
            event.setAnnotation(dto.getAnnotation());
        }

        if (dto.getDescription() != null) {
            event.setDescription(dto.getDescription());
        }

        if (dto.getEventDate() != null) {
            event.setEventDate(dto.getEventDate());
        }

        if (dto.getRequestModeration() != null) {
            event.setRequestModeration(dto.getRequestModeration());
        }

        if (dto.getLocation() != null) {
            event.setLocation(dto.getLocation());
        }

        if (dto.getPaid() != null) {
            event.setPaid(dto.getPaid());
        }

        if (dto.getParticipantLimit() != null) {
            event.setParticipantLimit(dto.getParticipantLimit());
        }

        if (dto.getTitle() != null) {
            event.setTitle(dto.getTitle());
        }

        if (dto.getCategory() != null) {
            event.setCategory(categoryRepository.findById(dto.getCategory()).orElseThrow());
        }

        if (dto.getStateAction() != null && dto.getStateAction().equals(StateAction.PUBLISH_EVENT) && event.getState().equals(State.PENDING)) {
            event.setState(State.PUBLISHED);
        }
        if (dto.getStateAction() != null && dto.getStateAction().equals(StateAction.REJECT_EVENT) && !event.getState().equals(State.PUBLISHED)) {
            event.setState(State.CANCELED);
        }

        return EventMapper.toFullDto(eventRepository.save(event));
    }

    private Long getViews(String start, String end, String uris, Boolean unique) {
        List<ViewStatsDto> dto = client.getStat(start, end, List.of(uris), unique);
        return dto.size() > 0 ? dto.get(0).getHits() : 0L;
    }

}
