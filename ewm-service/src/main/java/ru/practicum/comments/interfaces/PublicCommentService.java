package ru.practicum.comments.interfaces;

import ru.practicum.comments.dto.CommentDto;


import java.util.List;

public interface PublicCommentService {

    List<CommentDto> getCommentsByEventId(Long eventId);
}
