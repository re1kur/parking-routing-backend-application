package re1kur.pars.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import re1kur.core.dto.ParkingPlaceFullDto;
import re1kur.core.dto.ParkingPlaceShortDto;
import re1kur.core.dto.ParkingPlaceDto;
import re1kur.core.payload.ParkingPlacePayload;
import re1kur.pars.service.ParkingService;

@RestController
@RequestMapping("/api/places")
@RequiredArgsConstructor
public class ParkingController {
    private final ParkingService service;

    @PostMapping("/create")
    public ResponseEntity<ParkingPlaceShortDto> createParkingPlace(
            @RequestHeader(name = "Authorization") String token,
            @RequestBody @Valid ParkingPlacePayload payload) {
        ParkingPlaceShortDto body = service.create(payload, token);
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    @PutMapping("/{id}/clear")
    public ResponseEntity<ParkingPlaceDto> clearParkingPlace(
            @RequestHeader(name = "Authorization") String token,
            @PathVariable(name = "id") Integer number
    ) {
        ParkingPlaceDto body = service.clear(token, number);
        return ResponseEntity.ok(body);
    }

    @GetMapping("/{id}/short-information")
    public ResponseEntity<ParkingPlaceShortDto> getShortInformationParkingPlace(
            @PathVariable(name = "id") Integer number) {
        ParkingPlaceShortDto body = service.getShortByNumber(number);
        return ResponseEntity.ok(body);
    }

    @GetMapping("/{id}/information")
    public ResponseEntity<ParkingPlaceDto> getInformationParkingPlace(
            @PathVariable(name = "id") Integer number) {
        ParkingPlaceDto body = service.getByNumber(number);
        return ResponseEntity.ok(body);
    }

    @GetMapping("/{id}/full-information")
    public ResponseEntity<ParkingPlaceFullDto> getFullInformationParkingPlace(
            @PathVariable(name = "id") Integer number) {
        ParkingPlaceFullDto body = service.getFullByNumber(number);
        return ResponseEntity.ok(body);
    }
}
