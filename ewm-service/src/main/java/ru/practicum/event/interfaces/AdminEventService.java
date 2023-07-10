package ru.practicum.event.interfaces;

import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.UpdateEventAdminRequest;
import ru.practicum.event.enums.State;

import java.util.List;

public interface AdminEventService {

    List<EventFullDto> findEvents(List<Long> users, List<State> states,List<Long> categories,
                                  String rangeStart, String rangeEnd,Integer from, Integer size);

    EventFullDto update(Long eventId, UpdateEventAdminRequest dto);
}
