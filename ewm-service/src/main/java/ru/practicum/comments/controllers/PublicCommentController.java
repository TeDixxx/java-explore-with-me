package ru.practicum.comments.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.comments.dto.CommentDto;
import ru.practicum.comments.interfaces.PublicCommentService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comments")
public class PublicCommentController {
    private final PublicCommentService service;

    @GetMapping
    public List<CommentDto> getCommentByEventId(@RequestParam Long eventId) {
        return service.getCommentsByEventId(eventId);
    }


}
