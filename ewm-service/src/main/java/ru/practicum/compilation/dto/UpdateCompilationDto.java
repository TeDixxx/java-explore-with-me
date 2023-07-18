package ru.practicum.compilation.dto;


import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.Length;

import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@Builder
public class UpdateCompilationDto {

    List<Long> events;

    Boolean pinned;
    @Length(max = 50)
    String title;

}
