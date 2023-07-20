package ru.practicum.comments.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.comments.dto.CommentDto;
import ru.practicum.comments.interfaces.CommentRepository;
import ru.practicum.comments.interfaces.PublicCommentService;
import ru.practicum.comments.model.CommentMapper;
import ru.practicum.event.interfaces.EventRepository;

import ru.practicum.exceptions.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PublicCommentServiceImpl implements PublicCommentService {

    private final CommentRepository commentRepository;

    private final EventRepository eventRepository;


    @Override
    public List<CommentDto> getCommentsByEventId(Long eventId) {

        eventRepository.findById(eventId).orElseThrow(()
                -> new NotFoundException("Event not found"));

        return commentRepository.findAllByEventId(eventId).stream()
                .map(CommentMapper::toCommentDto)
                .collect(Collectors.toList());
    }
}
