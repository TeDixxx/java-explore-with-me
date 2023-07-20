package ru.practicum.comments.controllers;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.comments.dto.CommentDto;
import ru.practicum.comments.dto.NewCommentDto;
import ru.practicum.comments.dto.UpdateCommentRequest;
import ru.practicum.comments.interfaces.PrivateCommentService;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users/{userId}/comments")
public class PrivateCommentController {

    private final PrivateCommentService service;


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto addNewComment(@RequestBody @Valid NewCommentDto dto,
                                    @PathVariable Long userId) {

        return service.addNewComment(dto, userId);
    }

    @PatchMapping("/{commentId}")
    public CommentDto updateComment(@RequestBody @Valid UpdateCommentRequest updateComment,
                                    @PathVariable Long commentId,
                                    @PathVariable Long userId) {

        return service.updateComment(updateComment, commentId, userId);
    }

    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable Long commentId,
                              @PathVariable Long userId) {
        service.deleteComment(commentId, userId);
    }

    @PostMapping("/{commentId}/like")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto addLikeForComment(@PathVariable Long commentId,
                                        @PathVariable Long userId,
                                        @RequestParam Boolean isLike) {

        return service.addLikeOrDislikeForComment(commentId, userId, isLike);
    }

    @DeleteMapping("/{commentId}/delete")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteLikeOrDislike(@PathVariable Long commentId,
                                    @PathVariable Long userId,
                                    @RequestParam Boolean isLike) {

        service.deleteLikeOrDislike(commentId, userId, isLike);
    }
}
