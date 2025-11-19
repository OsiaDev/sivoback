package com.coljuegos.sivo.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CustomException extends RuntimeException {

    private final HttpStatus status;

    private CustomException() {
        this.status = HttpStatus.INTERNAL_SERVER_ERROR;
    }

    public CustomException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }

}
