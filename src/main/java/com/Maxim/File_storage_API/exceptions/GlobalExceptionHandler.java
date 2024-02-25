package com.Maxim.File_storage_API.exceptions;

import com.Maxim.File_storage_API.exceptions.service_exceptions.NotFoundError;
import com.Maxim.File_storage_API.exceptions.service_exceptions.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler
    public ResponseEntity<NotFoundError> entityNotExistException(NotFoundException e) {
        return new ResponseEntity<>(new NotFoundError(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

}