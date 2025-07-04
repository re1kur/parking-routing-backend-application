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
@RequestMapping("/api/parking/place")
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

    @PutMapping("/clear")
    public ResponseEntity<ParkingPlaceDto> clearParkingPlace(
            @RequestHeader(name = "Authorization") String token,
            @RequestParam(name = "number") Integer number
    ) {
        ParkingPlaceDto body = service.clear(token, number);
        return ResponseEntity.ok(body);
    }

    @GetMapping("/short-information")
    public ResponseEntity<ParkingPlaceShortDto> getShortInformationParkingPlace(
            @RequestParam(name = "number") Integer number) {
        ParkingPlaceShortDto body = service.getShortByNumber(number);
        return ResponseEntity.ok(body);
    }

    @GetMapping("/information")
    public ResponseEntity<ParkingPlaceDto> getInformationParkingPlace(
            @RequestParam(name = "number") Integer number) {
        ParkingPlaceDto body = service.getByNumber(number);
        return ResponseEntity.ok(body);
    }

    @GetMapping("/full-information")
    public ResponseEntity<ParkingPlaceFullDto> getFullInformationParkingPlace(
            @RequestParam(name = "number") Integer number) {
        ParkingPlaceFullDto body = service.getFullByNumber(number);
        return ResponseEntity.ok(body);
    }
}
