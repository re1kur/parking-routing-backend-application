package re1kur.pars.controller.place;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import re1kur.core.dto.ParkingPlaceFullDto;
import re1kur.core.dto.ParkingPlaceDto;
import re1kur.core.payload.ParkingPlacePayload;
import re1kur.pars.service.parking.PlaceService;

@RestController
@RequestMapping("/api/places/{id}")
@RequiredArgsConstructor
public class PlaceController {
    private final PlaceService service;

    // todo: edit policies

    @GetMapping
    public ResponseEntity<ParkingPlaceDto> getParkingPlace(
            @PathVariable(name = "id") Integer number) {
        ParkingPlaceDto body = service.getByNumber(number);
        return ResponseEntity.ok(body);
    }

    @PutMapping("/update")
    public ResponseEntity<ParkingPlaceDto> updatePlace(
            @PathVariable(name = "id") Integer number,
            @RequestHeader(name = "Authorization") String bearer,
            @RequestBody @Valid ParkingPlacePayload payload
            ) {
        ParkingPlaceDto body = service.update(number, payload, bearer);
        return ResponseEntity.ok(body);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deletePlace(
            @PathVariable(name = "id") Integer number,
            @RequestHeader(name = "Authorization") String bearer
    ) {
        service.delete(number, bearer);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/full")
    public ResponseEntity<ParkingPlaceFullDto> getFullInformationParkingPlace(
            @PathVariable(name = "id") Integer number) {
        ParkingPlaceFullDto body = service.getFullByNumber(number);
        return ResponseEntity.ok(body);
    }

}
