package org.francis.kcb_assessment.config.exception;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.francis.kcb_assessment.dto.response.ErrorResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestControllerAdvice
@ControllerAdvice
public class RestExceptionHandlerImpl {


    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<Object> handleConstraintViolation(final ConstraintViolationException ex, final WebRequest request) {
        final List<String> errors = new ArrayList<>();
        for (final ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            errors.add(String.format("%s %s", violation.getPropertyPath(), violation.getMessage()));
        }

        final ErrorResponse errorResponse = createErrorResponse(errors, HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(errorResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<Object> handleMethodArgumentNotValidException(final MethodArgumentNotValidException ex, final WebRequest request) {
        final List<String> errors = new ArrayList<>();
        for (final FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(String.format("%s %s", error.getField(), error.getDefaultMessage()));
        }
        for (final ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            errors.add(String.format("%s %s", error.getObjectName(), error.getDefaultMessage()));
        }
        final ErrorResponse errorResponse = createErrorResponse(errors, HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(errorResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({IllegalArgumentException.class})
    public ResponseEntity<Object> handleConstraintViolation(final IllegalArgumentException ex, final WebRequest request) {
        final ErrorResponse errorResponse = createErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(errorResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler({Exception.class})
    public ResponseEntity<Object> handleAll(final Exception ex, final WebRequest request) {
        log.error(ex.getMessage(), ex);
        final ErrorResponse errorResponse = createErrorResponse("error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(errorResponse, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ErrorResponse createErrorResponse(String error, HttpStatus httpStatus) {
        return new ErrorResponse(httpStatus, error);
    }

    private ErrorResponse createErrorResponse(List<String> errors, HttpStatus httpStatus) {
        return new ErrorResponse(httpStatus, errors);
    }

}
