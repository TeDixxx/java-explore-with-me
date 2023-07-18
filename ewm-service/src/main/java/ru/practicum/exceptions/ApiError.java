package ru.practicum.exceptions;


import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;


import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ApiError {

    List<String> errors;
    String message;
    String reason;
    HttpStatus status;
    String timestamp;
}
