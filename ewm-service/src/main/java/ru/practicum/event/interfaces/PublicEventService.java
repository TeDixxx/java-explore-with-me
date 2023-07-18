package ru.practicum.event.interfaces;

import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface PublicEventService {

    EventFullDto getEventById(Long eventId, HttpServletRequest request);

    List<EventShortDto> getEventsByParams(String text,
                                          List<Long> categories,
                                          Boolean paid,
                                          String rangeStart,
                                          String rangeEnd,
                                          Boolean onlyAvailable,
                                          String sort,
                                          int from,
                                          int size,
                                          HttpServletRequest request);
}
