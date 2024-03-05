package com.Maxim.File_storage_API.exceptions;

import com.Maxim.File_storage_API.exceptions.security_exeptions.AuthenticationError;
import com.Maxim.File_storage_API.exceptions.security_exeptions.InvalidCredentialsException;
import com.Maxim.File_storage_API.exceptions.security_exeptions.RegistrationError;
import com.Maxim.File_storage_API.exceptions.security_exeptions.UserNotAuthenticatedException;
import com.Maxim.File_storage_API.exceptions.service_exceptions.NotExistError;
import com.Maxim.File_storage_API.exceptions.service_exceptions.NotExistException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;


@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler
    public ResponseEntity<NotExistError> entityNotExistException(NotExistException e) {
        return new ResponseEntity<>(new NotExistError(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ResponseBody
    @ExceptionHandler
    public ResponseEntity<NotExistError> registrationException(RegistrationError e) {
        return new ResponseEntity<>(new NotExistError(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<AuthenticationError> invalidCredentials (InvalidCredentialsException e) {
        return new ResponseEntity<>(new AuthenticationError(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<AuthenticationError> notAuthenticated (UserNotAuthenticatedException e) {
        return new ResponseEntity<>(new AuthenticationError(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

}