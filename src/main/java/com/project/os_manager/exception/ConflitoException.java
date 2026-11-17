package com.project.os_manager.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


public class ConflitoException extends RuntimeException {
    public ConflitoException(String message) {
        super(message);
    }
}
