package re1kur.pars.controller.place;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import re1kur.core.dto.ParkingPlaceDto;
import re1kur.core.dto.ParkingPlaceShortDto;
import re1kur.core.payload.ParkingPlacePayload;
import re1kur.pars.service.parking.PlaceService;

import java.util.List;

@RestController
@RequestMapping("/api/places")
@RequiredArgsConstructor
public class ParkingPlacesController {
    private final PlaceService service;

    @PostMapping("/create")
    public ResponseEntity<ParkingPlaceShortDto> createParkingPlace(
            @RequestHeader(name = "Authorization") String token,
            @RequestBody @Valid ParkingPlacePayload payload
    ) {
        ParkingPlaceShortDto body = service.create(payload, token);
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    @GetMapping("/list")
    public ResponseEntity<List<ParkingPlaceDto>> getAll() {
        List<ParkingPlaceDto> body = service.getAll();

        return ResponseEntity.ok(body);
    }
}
