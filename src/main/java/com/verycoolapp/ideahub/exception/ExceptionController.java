package com.verycoolapp.ideahub.exception;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseBody
    public ResponseEntity<Object> handle(ConstraintViolationException violations) {
        List<Violation> validationErrors = violations.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .map(Violation::new)
                .collect(Collectors.toList());
        return new ResponseEntity<>(validationErrors, HttpStatus.BAD_REQUEST);
    }

}
