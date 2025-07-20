package re1kur.pars.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import re1kur.core.exception.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class ExceptionHandlerController {

    @ExceptionHandler
    public ResponseEntity<Map<String, Object>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult()
                .getFieldErrors().stream()
                .map(error -> "%s: %s"
                        .formatted(error.getField(), error.getDefaultMessage()))
                .toList();

        log.info("ADVICE VALIDATION-ERRORS: {}", errors);

        Map<String, Object> body = new HashMap<>();
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("errors", errors);

        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler
    public ResponseEntity<Map<String, Object>> handleMakeAlreadyExistsException(MakeAlreadyExistsException ex) {
        HttpStatus conflict = HttpStatus.CONFLICT;
        String message = ex.getMessage();
        log.info("ADVICE MAKE CONFLICT: {}", message);

        Map<String, Object> body = new HashMap<>();
        body.put("status", conflict.value());
        body.put("message", message);

        return ResponseEntity.status(conflict).body(body);
    }

    @ExceptionHandler
    public ResponseEntity<Map<String, Object>> handlerReservationAlreadyExistsException(ReservationAlreadyExistsException ex) {
        HttpStatus conflict = HttpStatus.CONFLICT;
        String message = ex.getMessage();
        log.info("ADVICE RESERVATION CONFLICT: {}", message);

        Map<String, Object> body = new HashMap<>();
        body.put("status", conflict.value());
        body.put("message", message);

        return ResponseEntity.status(conflict).body(body);
    }

    @ExceptionHandler
    public ResponseEntity<Map<String, Object>> handlePlaceAlreadyExistsException(PlaceAlreadyExistsException ex) {
        HttpStatus conflict = HttpStatus.CONFLICT;
        String message = ex.getMessage();
        log.info("ADVICE PLACE CONFLICT: {}", message);

        Map<String, Object> body = new HashMap<>();
        body.put("status", conflict.value());
        body.put("message", message);

        return ResponseEntity.status(conflict).body(body);
    }

    @ExceptionHandler
    public ResponseEntity<Map<String, Object>> handleCarAlreadyExistsException(CarAlreadyExistsException ex) {
        HttpStatus conflict = HttpStatus.CONFLICT;
        String message = ex.getMessage();
        log.info("ADVICE CAR CONFLICT: {}", message);

        Map<String, Object> body = new HashMap<>();
        body.put("status", conflict.value());
        body.put("message", message);

        return ResponseEntity.status(conflict).body(body);
    }

    @ExceptionHandler
    public ResponseEntity<Map<String, Object>> handleRegionAlreadyExistsException(RegionAlreadyExistsException ex) {
        HttpStatus conflict = HttpStatus.CONFLICT;
        String message = ex.getMessage();
        log.info("ADVICE REGION CONFLICT: {}", message);

        Map<String, Object> body = new HashMap<>();
        body.put("status", conflict.value());
        body.put("message", message);

        return ResponseEntity.status(conflict).body(body);
    }

    @ExceptionHandler
    public ResponseEntity<Map<String, Object>> handleRegionCodeAlreadyExistsException(RegionCodeAlreadyExistsException ex) {
        HttpStatus conflict = HttpStatus.CONFLICT;
        String message = ex.getMessage();
        log.info("ADVICE REGION CODE CONFLICT: {}", message);

        Map<String, Object> body = new HashMap<>();
        body.put("status", conflict.value());
        body.put("message", message);

        return ResponseEntity.status(conflict).body(body);
    }

    @ExceptionHandler
    public ResponseEntity<Map<String, Object>> handleMakeNotFoundException(MakeNotFoundException ex) {
        HttpStatus badRequest = HttpStatus.BAD_REQUEST;
        String message = ex.getMessage();
        log.info("ADVICE MAKE NOT FOUND: {}", message);

        Map<String, Object> body = new HashMap<>();
        body.put("status", badRequest.value());
        body.put("message", message);

        return ResponseEntity.status(badRequest).body(body);
    }

    @ExceptionHandler
    public ResponseEntity<Map<String, Object>> handlePlaceNotFoundException(PlaceNotFoundException ex) {
        HttpStatus badRequest = HttpStatus.BAD_REQUEST;
        String message = ex.getMessage();
        log.info("ADVICE PLACE NOT FOUND: {}", message);

        Map<String, Object> body = new HashMap<>();
        body.put("status", badRequest.value());
        body.put("message", message);

        return ResponseEntity.status(badRequest).body(body);
    }

    @ExceptionHandler
    public ResponseEntity<Map<String, Object>> handleCarNotFoundException(CarNotFoundException ex) {
        HttpStatus badRequest = HttpStatus.BAD_REQUEST;
        String message = ex.getMessage();
        log.info("ADVICE CAR NOT FOUND: {}", message);

        Map<String, Object> body = new HashMap<>();
        body.put("status", badRequest.value());
        body.put("message", message);

        return ResponseEntity.status(badRequest).body(body);
    }

    @ExceptionHandler
    public ResponseEntity<Map<String, Object>> handleRegionNotFoundException(RegionNotFoundException ex) {
        HttpStatus badRequest = HttpStatus.BAD_REQUEST;
        String message = ex.getMessage();
        log.info("ADVICE REGION NOT FOUND: {}", message);

        Map<String, Object> body = new HashMap<>();
        body.put("status", badRequest.value());
        body.put("message", message);

        return ResponseEntity.status(badRequest).body(body);
    }

    @ExceptionHandler
    public ResponseEntity<Map<String, Object>> handleReservationNotFoundException(ReservationNotFoundException ex) {
        HttpStatus badRequest = HttpStatus.BAD_REQUEST;
        String message = ex.getMessage();
        log.info("ADVICE RESERVATION NOT FOUND: {}", message);

        Map<String, Object> body = new HashMap<>();
        body.put("status", badRequest.value());
        body.put("message", message);

        return ResponseEntity.status(badRequest).body(body);
    }

    @ExceptionHandler
    public ResponseEntity<Map<String, Object>> handleUserDoesNotHavePermissionForEndpoint(UserDoesNotHavePermissionForEndpoint ex) {
        HttpStatus badRequest = HttpStatus.FORBIDDEN;
        String message = ex.getMessage();
        log.info("ADVICE USER FORBIDDEN: {}", message);

        Map<String, Object> body = new HashMap<>();
        body.put("status", badRequest.value());
        body.put("message", message);

        return ResponseEntity.status(badRequest).body(body);
    }
}