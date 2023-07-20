package ru.practicum.comments.like.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import ru.practicum.comments.model.Comment;
import ru.practicum.user.model.User;

import javax.persistence.*;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "reactions")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Reaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User owner;

    @ManyToOne
    @JoinColumn(name = "comment_id")
    Comment comment;

    Boolean isLike;
}
