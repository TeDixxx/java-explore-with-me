package ru.practicum.comments.interfaces;

import ru.practicum.comments.dto.CommentDto;
import ru.practicum.comments.dto.NewCommentDto;
import ru.practicum.comments.dto.UpdateCommentRequest;

public interface PrivateCommentService {

    CommentDto addNewComment(NewCommentDto dto, Long userId);

    CommentDto updateComment(UpdateCommentRequest updateComment, Long commentId, Long authorId);

    void deleteComment(Long commentId, Long userId);

    CommentDto addLikeOrDislikeForComment(Long commentId, Long userId, Boolean isLike);

     void deleteLikeOrDislike(Long commentId, Long userId,Boolean isLike);


}
