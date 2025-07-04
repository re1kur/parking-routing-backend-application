package re1kur.pars.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import re1kur.core.dto.ParkingPlaceReservationDto;
import re1kur.core.payload.ParkingPlaceReservationPayload;
import re1kur.pars.service.ReservationService;

@RestController
@RequestMapping("/api/parking/reservation")
@RequiredArgsConstructor
public class ReservationController {
    private final ReservationService service;

    @PostMapping("create")
    public ResponseEntity<ParkingPlaceReservationDto> createReservation(
            @RequestHeader(name = "Authorization") String token,
            @RequestBody @Valid ParkingPlaceReservationPayload payload
    ) {
        ParkingPlaceReservationDto body = service.create(token, payload);
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }


}
