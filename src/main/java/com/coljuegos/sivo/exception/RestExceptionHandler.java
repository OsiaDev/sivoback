package com.coljuegos.sivo.exception;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(UsernameNotFoundException.class)
    protected ResponseEntity<Object> handleEntityNotFound(UsernameNotFoundException exception) {
        ApiError error = new ApiError(HttpStatus.NOT_FOUND);
        error.setMessage(exception.getMessage());
        return this.buildResponseEntity(error);
    }

    @ExceptionHandler(CustomException.class)
    protected ResponseEntity<Object> handleEntityNotFound(CustomException exception) {
        ApiError error = new ApiError(exception.getStatus());
        error.setMessage(exception.getMessage());
        return this.buildResponseEntity(error);
    }

    @ExceptionHandler(BadCredentialsException.class)
    protected ResponseEntity<Object> handleEntityNotFound(BadCredentialsException exception) {
        ApiError error = new ApiError(HttpStatus.UNAUTHORIZED);
        error.setMessage(exception.getMessage());
        return this.buildResponseEntity(error);
    }

    private ResponseEntity<Object> buildResponseEntity(ApiError error) {
        return new ResponseEntity<>(error, error.getStatus());
    }

}
