package ru.practicum.event.controllers;


import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.interfaces.PublicEventService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/events")
public class PublicEventController {


    private final PublicEventService service;

    @GetMapping
    public List<EventShortDto> getEventsByParams(@RequestParam(required = false) String text,
                                                 @RequestParam(required = false) List<Long> categories,
                                                 @RequestParam(required = false) Boolean paid,
                                                 @RequestParam(required = false) String rangeStart,
                                                 @RequestParam(required = false) String rangeEnd,
                                                 @RequestParam(required = false, defaultValue = "false") Boolean onlyAvailable,
                                                 @RequestParam(required = false, defaultValue = "EVENT_DATE") String sort,
                                                 @RequestParam(name = "from", defaultValue = "0") int from,
                                                 @RequestParam(name = "size", defaultValue = "10") int size,
                                                 HttpServletRequest request) {
        return service.getEventsByParams(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size, request);
    }

    @GetMapping("/{eventId}")
    public EventFullDto getEventById(@PathVariable Long eventId, HttpServletRequest request) {
        return service.getEventById(eventId, request);
    }
}
