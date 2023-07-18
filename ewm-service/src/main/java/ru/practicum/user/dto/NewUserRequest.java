package ru.practicum.user.dto;


import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NewUserRequest {
    @Email
    @NotBlank
    @Length(min = 6, max = 254)
    String email;
    @NotNull
    @NotBlank
    @Length(min = 2, max = 250)
    String name;
}
