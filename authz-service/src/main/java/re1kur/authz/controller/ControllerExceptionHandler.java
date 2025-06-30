package re1kur.authz.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import re1kur.core.exception.EndpointNotFoundException;
import re1kur.core.exception.IdentityServiceAuthenticationException;
import re1kur.core.exception.TokenDidNotPassVerificationException;
import re1kur.core.exception.UserDoesNotHavePermissionForEndpoint;

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

    @ExceptionHandler(UserDoesNotHavePermissionForEndpoint.class)
    public ResponseEntity<Void> handleUserDoesNotHavePermissionForEndpoint(UserDoesNotHavePermissionForEndpoint ex) {
        log.info(ex.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @ExceptionHandler(EndpointNotFoundException.class)
    public ResponseEntity<Void> handleEndpointNotFoundException(EndpointNotFoundException ex) {
        log.info(ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @ExceptionHandler(TokenDidNotPassVerificationException.class)
    public ResponseEntity<Void> handleTokenDidNotPassVerificationException(TokenDidNotPassVerificationException ex) {
        log.info(ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
