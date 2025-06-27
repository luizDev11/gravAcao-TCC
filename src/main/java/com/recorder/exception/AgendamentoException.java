package com.recorder.exception;


import org.springframework.http.HttpStatus;

public class AgendamentoException extends RuntimeException {
    private final HttpStatus status;

    public AgendamentoException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public AgendamentoException(String message, HttpStatus status, Throwable cause) {
        super(message, cause);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}