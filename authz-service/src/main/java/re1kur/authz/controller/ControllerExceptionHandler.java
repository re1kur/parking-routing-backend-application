package re1kur.authz.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import re1kur.core.exception.IdentityServiceAuthenticationException;

@Slf4j
@RestControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(IdentityServiceAuthenticationException.class)
    public ResponseEntity<String> handleIdentityServiceAuthenticationException(IdentityServiceAuthenticationException ex) {
        log.error(ex.getMessage());
        return ResponseEntity
                .status(ex.getHttpStatusCode())
                .body(ex.getBody());
    }
}
