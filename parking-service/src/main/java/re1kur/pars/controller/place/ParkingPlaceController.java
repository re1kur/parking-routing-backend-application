package re1kur.pars.controller.place;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import re1kur.core.dto.ParkingPlaceDto;
import re1kur.core.dto.ParkingPlaceFullDto;
import re1kur.core.dto.ParkingPlaceShortDto;
import re1kur.pars.service.parking.PlaceService;

@RestController
@RequestMapping("/api/places/{id}")
@RequiredArgsConstructor
public class ParkingPlaceController {
    private final PlaceService service;

    @GetMapping
    public ResponseEntity<ParkingPlaceDto> getInformationParkingPlace(
            @PathVariable(name = "id") Integer number) {
        ParkingPlaceDto body = service.getByNumber(number);
        return ResponseEntity.ok(body);
    }

    @GetMapping("/short")
    public ResponseEntity<ParkingPlaceShortDto> getShortInformationParkingPlace(
            @PathVariable(name = "id") Integer number) {
        ParkingPlaceShortDto body = service.getShortByNumber(number);
        return ResponseEntity.ok(body);
    }

    @GetMapping("/full")
    public ResponseEntity<ParkingPlaceFullDto> getFullInformationParkingPlace(
            @PathVariable(name = "id") Integer number) {
        ParkingPlaceFullDto body = service.getFullByNumber(number);
        return ResponseEntity.ok(body);
    }

    @PutMapping("/clear")
    public ResponseEntity<ParkingPlaceDto> clearParkingPlace(
            @RequestHeader(name = "Authorization") String token,
            @PathVariable(name = "id") Integer number
    ) {
        ParkingPlaceDto body = service.clear(token, number);
        return ResponseEntity.ok(body);
    }
}
