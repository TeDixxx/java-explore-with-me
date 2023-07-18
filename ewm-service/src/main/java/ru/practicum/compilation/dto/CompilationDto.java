package ru.practicum.compilation.dto;


import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.event.dto.EventShortDto;

import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CompilationDto {

    Long id;

    List<EventShortDto> events;

    Boolean pinned;

    String title;
}
