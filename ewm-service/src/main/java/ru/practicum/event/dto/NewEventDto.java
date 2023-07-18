package ru.practicum.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.Length;
import ru.practicum.event.model.Location;


import javax.validation.constraints.NotNull;

import java.time.LocalDateTime;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewEventDto {

    @Length(min = 20, max = 2000)
    @NotNull
    String annotation;

    @NotNull
    Long category;

    @NotNull
    @Length(min = 20, max = 7000)
    String description;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime eventDate;

    @NotNull
    Location location;

    Boolean paid;

    Long participantLimit;

    Boolean requestModeration;

    @NotNull
    @Length(min = 3, max = 120)
    String title;

}
