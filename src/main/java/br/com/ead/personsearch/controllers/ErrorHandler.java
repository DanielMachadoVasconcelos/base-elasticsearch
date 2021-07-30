package br.com.ead.personsearch.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.SneakyThrows;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import static org.apache.http.HttpStatus.SC_INTERNAL_SERVER_ERROR;

import static br.com.ead.personsearch.controllers.ErrorHandler.ErrorMessage.*;
import static org.apache.commons.lang3.exception.ExceptionUtils.getRootCause;

import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
@AllArgsConstructor
public class ErrorHandler extends ResponseEntityExceptionHandler  {

//    @Value("${application.version}")
    public static final String APPLICATION_VERSION = "1.0.0.0";
    public static final String UNHANDLED_EXCEPTIONS_METRIC_NAME = "exception.unhandled";

    ObjectMapper objectMapper;
    MeterRegistry meterRegistry;

    // 400

    @Override
    @SneakyThrows
    protected ResponseEntity<Object> handleMethodArgumentNotValid(final MethodArgumentNotValidException ex, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
        var bodyOfResponse = objectMapper.writeValueAsString(new ErrorMessage(ex.getBindingResult().getAllErrors().stream()
                .map(error -> new ErrorDetails(((FieldError) error).getField(), error.getDefaultMessage()))
                .collect(Collectors.toList())));
        headers.add("timestamp", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        headers.add("version", APPLICATION_VERSION);
        return handleExceptionInternal(ex, bodyOfResponse, headers, HttpStatus.BAD_REQUEST, request);
    }

    //500

    @SneakyThrows
    @ResponseBody
    @ExceptionHandler(Throwable.class)
    protected ResponseEntity<Object> catchAllException(final Throwable ex, final HttpHeaders headers, final WebRequest request) {
        var nameTage = Tag.of("name", ex.getClass().getSimpleName().toLowerCase());
        var causeTag = Tag.of("cause", getRootCause(ex).getClass().getSimpleName().toLowerCase());
        meterRegistry.counter(UNHANDLED_EXCEPTIONS_METRIC_NAME, List.of(nameTage, causeTag)).increment();

        headers.add("timestamp", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        headers.add("version", APPLICATION_VERSION);
        return ResponseEntity.status(SC_INTERNAL_SERVER_ERROR).headers(headers).build();
    }

    @Data
    @Builder
    @AllArgsConstructor
    public static class ErrorMessage {
        List<ErrorDetails> errors;

        @Data
        @Builder
        @AllArgsConstructor
        public static class ErrorDetails {
            String field;
            String message;
        }
    }
}
