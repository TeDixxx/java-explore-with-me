package ru.practicum.compilation.dto;


import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewCompilationDto {

    List<Long> events;

    Boolean pinned;

    @NotNull
    @NotBlank
    @Length(max = 50)
    String title;
}
