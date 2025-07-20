package re1kur.pars.controller.place;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import re1kur.core.dto.ParkingPlaceFullDto;
import re1kur.core.dto.PlaceDto;
import re1kur.core.payload.PlacePayload;
import re1kur.pars.service.parking.PlaceService;

@RestController
@RequestMapping("/api/places/{id}")
@RequiredArgsConstructor
public class PlaceController {
    private final PlaceService service;

    @GetMapping
    public ResponseEntity<PlaceDto> getParkingPlace(
            @PathVariable(name = "id") Integer number) {
        PlaceDto body = service.getByNumber(number);
        return ResponseEntity.ok(body);
    }

    @PutMapping("/update")
    public ResponseEntity<PlaceDto> updatePlace(
            @PathVariable(name = "id") Integer number,
            @RequestHeader(name = "Authorization") String bearer,
            @RequestBody @Valid PlacePayload payload
            ) {
        PlaceDto body = service.update(number, payload, bearer);
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
