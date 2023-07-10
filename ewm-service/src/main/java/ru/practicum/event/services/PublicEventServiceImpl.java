package ru.practicum.event.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.StatsClient;
import ru.practicum.category.interfaces.CategoryRepository;
import ru.practicum.category.model.Category;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.enums.State;
import ru.practicum.event.interfaces.EventRepository;
import ru.practicum.event.interfaces.PublicEventService;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.EventMapper;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.model.EndpointHitDto;
import ru.practicum.model.ViewStatsDto;
import ru.practicum.request.interfaces.RequestRepository;
import ru.practicum.request.model.RequestStatus;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PublicEventServiceImpl implements PublicEventService {

    private final EventRepository eventRepository;

    private final RequestRepository requestRepository;

    private final CategoryRepository categoryRepository;

    private final StatsClient client;

    @Override
    public EventFullDto getEventById(Long eventId, HttpServletRequest request) {

//        client.saveStats(new EndpointHitDto(null,
//                "ewm-service",
//                request.getRequestURI(),
//                request.getRemoteAddr(),
//                LocalDateTime.now())
//        );

        Event event = eventRepository.findById(eventId).orElseThrow(()
                -> new NotFoundException("event not found"));

        eventRepository.save(event);

        return EventMapper.toFullDto(event);
    }

    @Override
    public List<EventShortDto> getEventsByParams(String text,
                                                 List<Long> categories,
                                                 Boolean paid,
                                                 String rangeStart,
                                                 String rangeEnd,
                                                 Boolean onlyAvailable,
                                                 String sort, int from,
                                                 int size, HttpServletRequest request) {

//        client.saveStats(new EndpointHitDto(null,
//                "ewm-service",
//                "/events",
//                request.getRemoteAddr(),
//                LocalDateTime.now())
//        );
        LocalDateTime start = LocalDateTime.parse(rangeStart, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        LocalDateTime end = LocalDateTime.parse(rangeEnd, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        if (rangeStart.isEmpty()) {
            start = LocalDateTime.now().plusHours(3);
        }

        if (rangeEnd.isEmpty()) {
            end = LocalDateTime.now().plusDays(2);
        }

        if (categories == null || categories.isEmpty()) {
            categories = categoryRepository.findAll()
                    .stream()
                    .map(Category::getId)
                    .collect(Collectors.toList());
        }

        Pageable pageable = PageRequest.of(from, size);

        List<Event> publicEvents = eventRepository.searchPublicEvents(text, categories, paid, start, end, State.PUBLISHED,
                pageable);

        if (onlyAvailable) {
            publicEvents = publicEvents.stream()
                    .filter(e -> e.getParticipantLimit() > confirmed(e.getId()))
                    .collect(Collectors.toList());
        }

        List<EventShortDto> eventShortDto = publicEvents.stream()
                .map(EventMapper::toShortDto)
                //.peek(e -> e.setViews(viewsEvent(rangeStart, rangeEnd, "/events/" + e.getId(), false)))
                .collect(Collectors.toList());

        if (sort.equals("VIEWS")) {
            eventShortDto.stream()
                    .sorted(Comparator.comparing(EventShortDto::getViews));
        }


        return eventShortDto;
    }

    private Long confirmed(Long eventId) {
        return requestRepository.countByEventIdAndStatus(eventId, RequestStatus.CONFIRMED);
    }

//    private Long viewsEvent(String rangeStart, String rangeEnd, String uris, Boolean unique) {
//        List<ViewStatsDto> dto = client.getStat(rangeStart, rangeEnd, List.of(uris), unique);
//        return dto.size() > 0 ? dto.get(0).getHits() : 0L;
//    }
}
