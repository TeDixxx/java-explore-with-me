package ru.practicum.comments.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.comments.dto.CommentDto;
import ru.practicum.comments.dto.NewCommentDto;
import ru.practicum.comments.dto.UpdateCommentRequest;
import ru.practicum.comments.interfaces.CommentRepository;
import ru.practicum.comments.interfaces.PrivateCommentService;
import ru.practicum.comments.like.model.Reaction;
import ru.practicum.comments.like.repository.LikeRepository;
import ru.practicum.comments.model.Comment;
import ru.practicum.comments.model.CommentMapper;
import ru.practicum.event.interfaces.EventRepository;
import ru.practicum.event.model.Event;
import ru.practicum.exceptions.BadRequestException;
import ru.practicum.exceptions.ConflictException;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.user.interfaces.UserRepository;
import ru.practicum.user.model.User;

import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
public class PrivateCommentServiceImpl implements PrivateCommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final LikeRepository likeRepository;


    @Override
    public CommentDto addNewComment(NewCommentDto dto, Long userId) {

        Event event = eventRepository.findById(dto.getEventId()).orElseThrow(()
                -> new NotFoundException("Event not found"));

        User author = userRepository.findById(userId).orElseThrow(()
                -> new NotFoundException("User not found"));

        Comment comment = CommentMapper.toComment(dto, event, author);

        comment.setLikeCount(0L);
        comment.setDislikeCount(0L);
        comment.setCreated(LocalDateTime.now());

        return CommentMapper.toCommentDto(commentRepository.save(comment));

    }

    @Override
    public CommentDto updateComment(UpdateCommentRequest updateComment, Long commentId, Long authorId) {

        Comment comment = commentRepository.findById(commentId).orElseThrow(()
                -> new NotFoundException("Comment not found"));

        if (!comment.getAuthor().getId().equals(authorId)) {
            throw new BadRequestException("Only author can change comment");
        }

        if (!comment.getEvent().getId().equals(updateComment.getEventId())) {
            throw new BadRequestException("The event is missing this comment");
        }

        if (updateComment.getText() != null) {
            comment.setText(updateComment.getText());
        }

        return CommentMapper.toCommentDto(commentRepository.save(comment));
    }

    @Override
    public void deleteComment(Long commentId, Long userId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(()
                -> new NotFoundException("Comment not found"));

        if (!comment.getAuthor().getId().equals(userId)) {
            throw new ConflictException("Only author can delete comment");
        }

        commentRepository.deleteById(commentId);
    }

    @Override
    public CommentDto addLikeOrDislikeForComment(Long commentId, Long userId, Boolean isLike) {

        Comment comment = commentRepository.findById(commentId).orElseThrow(()
                -> new NotFoundException("Comment not found"));

        User user = userRepository.findById(userId).orElseThrow(()
                -> new NotFoundException("User not found"));

        if (likeRepository.existsByCommentAndOwner(comment, user)) {
            throw new BadRequestException("This user can't put more");
        }

        likeRepository.save(Reaction.builder()
                .owner(user)
                .comment(comment)
                .isLike(isLike)
                .build());

        if (isLike != null) {
            if (isLike.equals(true)) {
                comment.setLikeCount(comment.getLikeCount() + 1);
            } else {
                comment.setDislikeCount(comment.getDislikeCount() + 1);
            }
        }

        return CommentMapper.toCommentDto(comment);

    }

    @Override
    @Transactional
    public void deleteLikeOrDislike(Long commentId, Long userId, Boolean isLike) {

        Comment comment = commentRepository.findById(commentId).orElseThrow(()
                -> new NotFoundException("Comment not found"));

        User user = userRepository.findById(userId).orElseThrow(()
                -> new NotFoundException("User not found"));

        if (!likeRepository.existsByCommentAndOwner(comment, user)) {
            throw new BadRequestException("This user can't put more");
        }

        likeRepository.deleteByCommentAndOwner(comment, user);

        if (isLike != null) {
            if (isLike.equals(true)) {
                if (comment.getLikeCount() >= 1) {
                    comment.setLikeCount(comment.getLikeCount() - 1);
                }

            } else {
                if (comment.getDislikeCount() >= 1) {
                    comment.setDislikeCount(comment.getDislikeCount() - 1);
                }
            }
        }

    }
}
