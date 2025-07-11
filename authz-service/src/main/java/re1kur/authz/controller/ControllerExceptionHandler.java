package re1kur.authz.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import re1kur.core.exception.*;

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

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<Void> handleInvalidTokenException(InvalidTokenException ex) {
        log.info(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @ExceptionHandler(UserDoesNotHavePermissionForEndpoint.class)
    public ResponseEntity<Void> handleUserDoesNotHavePermissionForEndpoint(UserDoesNotHavePermissionForEndpoint ex) {
        log.info(ex.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @ExceptionHandler(EndpointNotFoundException.class)
    public ResponseEntity<Void> handleEndpointNotFoundException(EndpointNotFoundException ex) {
        log.info(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @ExceptionHandler(TokenDidNotPassVerificationException.class)
    public ResponseEntity<Void> handleTokenDidNotPassVerificationException(TokenDidNotPassVerificationException ex) {
        log.info(ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Void> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        log.info(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
}
