package ru.practicum.comments.model;

import ru.practicum.comments.dto.CommentDto;
import ru.practicum.comments.dto.NewCommentDto;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.EventMapper;
import ru.practicum.user.model.User;
import ru.practicum.user.model.UserMapper;

import java.time.LocalDateTime;

public class CommentMapper {

    public static Comment toComment(NewCommentDto dto, Event event, User author) {
        return Comment.builder()
                .text(dto.getText())
                .author(author)
                .event(event)
                .created(LocalDateTime.now())
                .build();
    }

    public static CommentDto toCommentDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .author(UserMapper.toShortUSer(comment.getAuthor()))
                .event(EventMapper.toShortDto(comment.getEvent()))
                .likeCount(comment.getLikeCount())
                .dislikeCount(comment.getDislikeCount())
                .created(comment.getCreated())
                .build();
    }
}
