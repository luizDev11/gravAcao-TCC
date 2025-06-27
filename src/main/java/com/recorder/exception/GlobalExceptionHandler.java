package com.recorder.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(AgendamentoException.class)
    public ResponseEntity<String> handleAgendamentoException(AgendamentoException e) {
        logger.error("Erro de agendamento: {}", e.getMessage());
        return ResponseEntity.status(e.getStatus()).body(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(Exception e) {
        logger.error("Erro interno: {}", e.getMessage(), e);
        return ResponseEntity.internalServerError().body("Ocorreu um erro interno");
    }
}
