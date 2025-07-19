package re1kur.pars.controller.reservation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import re1kur.core.dto.ReservationDto;
import re1kur.core.dto.ReservationFullDto;
import re1kur.pars.service.parking.ReservationService;

import java.util.UUID;

@RestController
@RequestMapping("/api/reservations/{id}")
@RequiredArgsConstructor
public class ReservationController {
    private final ReservationService service;

    // todo: policy
    @GetMapping
    public ResponseEntity<ReservationDto> getReservation(
            @RequestHeader(name = "Authorization") String bearer,
            @PathVariable(name = "id") UUID id
    ) {
        ReservationDto body = service.getById(id, bearer);
        return ResponseEntity.ok(body);
    }

    @GetMapping("/full")
    public ResponseEntity<ReservationFullDto> getReservationFull(
            @RequestHeader(name = "Authorization") String bearer,
            @PathVariable(name = "id") UUID id
    ) {
        ReservationFullDto body = service.getFullById(id, bearer);
        return ResponseEntity.ok(body);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteReservation(
            @RequestHeader(name = "Authorization") String bearer,
            @PathVariable(name = "id") UUID id
    ) {
        service.deleteById(id, bearer);
        return ResponseEntity.noContent().build();
    }
}
