package ru.practicum.comments.like.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.comments.like.model.Reaction;
import ru.practicum.comments.model.Comment;
import ru.practicum.user.model.User;

@Repository
public interface LikeRepository extends JpaRepository<Reaction, Long> {

    Boolean existsByCommentAndOwner(Comment comment, User owner);

    void deleteByCommentAndOwner(Comment comment, User user);

}
