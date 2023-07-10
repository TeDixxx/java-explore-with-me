package ru.practicum.event.controllers;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.UpdateEventAdminRequest;
import ru.practicum.event.enums.State;
import ru.practicum.event.interfaces.AdminEventService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/events")
public class AdminEventController {

    private final AdminEventService service;


    @GetMapping
    public List<EventFullDto> findEvents(@RequestParam(required = false) List<Long> users,
                                         @RequestParam(required = false) List<State> states,
                                         @RequestParam(required = false) List<Long> categories,
                                         @RequestParam(required = false) String rangeStart,
                                         @RequestParam(required = false) String rangeEnd,
                                         @RequestParam(name = "from", defaultValue = "0") int from,
                                         @RequestParam(name = "size", defaultValue = "10") int size) {

        return service.findEvents(users, states, categories, rangeStart, rangeEnd, from, size);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto update(@PathVariable Long eventId, @RequestBody UpdateEventAdminRequest dto) {
        return service.update(eventId, dto);
    }

}
