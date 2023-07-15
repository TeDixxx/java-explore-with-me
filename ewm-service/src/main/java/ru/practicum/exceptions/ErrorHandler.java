package ru.practicum.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.util.Collections;


@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw);

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleBadRequest(final MethodArgumentNotValidException e) {
        return badRequest(e);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleBadRequest(final MethodArgumentTypeMismatchException e) {
        return badRequest(e);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleBadRequest(final MissingServletRequestParameterException e) {
        return badRequest(e);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleConflict(final DataIntegrityViolationException e) {
        return conflict(e);
    }


    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleNotFound(final NotFoundException e) {
        log.warn("404 {}", e.getMessage());
        e.printStackTrace(pw);
        return ApiError.builder()
                .errors(Collections.singletonList(sw.toString()))
                .status(HttpStatus.NOT_FOUND)
                .reason("not found")
                .message(e.getMessage())
                .timestamp(LocalDateTime.now().toString())
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleNotFound(final ConflictException e) {
        log.warn("409 {}", e.getMessage());
        e.printStackTrace(pw);
        return ApiError.builder()
                .errors(Collections.singletonList(sw.toString()))
                .status(HttpStatus.CONFLICT)
                .reason("not found")
                .message(e.getMessage())
                .timestamp(LocalDateTime.now().toString())
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleNotFound(final BadRequestException badRequestException) {
        log.warn("400 {}", badRequestException.getMessage());
        badRequestException.printStackTrace(pw);
        return ApiError.builder()
                .errors(Collections.singletonList(sw.toString()))
                .status(HttpStatus.BAD_REQUEST)
                .reason("bad request")
                .message(badRequestException.getMessage())
                .timestamp(LocalDateTime.now().toString())
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError handleInternalServerError(final Throwable throwable) {
        log.error("500 {}", throwable.getMessage(), throwable);
        throwable.printStackTrace(pw);
        return ApiError.builder()
                .errors(Collections.singletonList(sw.toString()))
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .reason("error")
                .message(throwable.getMessage())
                .timestamp(LocalDateTime.now().toString())
                .build();
    }


    private ApiError badRequest(final Exception e) {
        log.error("400 {}", e.getMessage());
        e.printStackTrace(pw);
        return ApiError.builder()
                .errors(Collections.singletonList(sw.toString()))
                .status(HttpStatus.BAD_REQUEST)
                .reason("Incorrect request")
                .message(e.getMessage())
                .timestamp(LocalDateTime.now().toString())
                .build();
    }

    private ApiError conflict(final Exception e) {
        log.error("409 {}", e.getMessage());
        e.printStackTrace(pw);
        return ApiError.builder()
                .errors(Collections.singletonList(sw.toString()))
                .status(HttpStatus.CONFLICT)
                .reason("conflict")
                .message(e.getMessage())
                .timestamp(LocalDateTime.now().toString())
                .build();
    }

}
