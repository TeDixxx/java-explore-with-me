package ru.practicum.comments.dto;


import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;

import javax.validation.constraints.Size;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewCommentDto {

    @NotBlank
    @Size(max = 1000)
    String text;

    Long eventId;
}
