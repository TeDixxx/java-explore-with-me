package ru.practicum.user.dto;


import lombok.*;
import lombok.experimental.FieldDefaults;


@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class UserDto {
    Long id;
    String name;
    String email;

}
