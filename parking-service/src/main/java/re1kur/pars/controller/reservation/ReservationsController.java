package re1kur.pars.controller.reservation;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import re1kur.core.dto.PageDto;
import re1kur.core.dto.ReservationDto;
import re1kur.core.dto.ReservationFullDto;
import re1kur.core.payload.ReservationPayload;
import re1kur.pars.service.parking.ReservationService;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationsController {
    private final ReservationService service;

    @PostMapping("/create")
    public ResponseEntity<ReservationFullDto> createReservation(
            @RequestHeader(name = "Authorization") String bearer,
            @RequestBody @Valid ReservationPayload payload
    ) {
        ReservationFullDto body = service.create(bearer, payload);
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    @GetMapping("/list")
    public ResponseEntity<PageDto<ReservationDto>> getMyReservations(
            @RequestHeader(name = "Authorization") String token,
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "size", defaultValue = "10") Integer size,
            @RequestParam(name = "date", required = false) LocalDate date
            ) {
        Pageable pageable = PageRequest.of(page, size);
        PageDto<ReservationDto> body = service.getPageByUserId(token, pageable, date);
        return ResponseEntity.ok(body);
    }
}