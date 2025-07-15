package re1kur.pars.controller.place;

import jakarta.validation.Valid;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import re1kur.core.dto.ParkingPlaceDto;
import re1kur.core.payload.ParkingPlacePayload;
import re1kur.pars.service.parking.PlaceService;

import java.util.List;

@RestController
@RequestMapping("/api/places")
@RequiredArgsConstructor
public class PlacesController {
    private final PlaceService service;

    @PostMapping("/create")
    public ResponseEntity<ParkingPlaceDto> createParkingPlace(
            @RequestHeader(name = "Authorization") String bearer,
            @RequestBody @Valid ParkingPlacePayload payload
    ) {
        ParkingPlaceDto body = service.create(payload, bearer);
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }


    @GetMapping("/list")
    public ResponseEntity<List<ParkingPlaceDto>> getList(
            @RequestParam(name = "page", required = false, defaultValue = "0") @PositiveOrZero Integer page,
            @RequestParam(name = "size", required = false, defaultValue = "10") Integer size
    ) {
        List<ParkingPlaceDto> body = service.getPage(page, size);

        return ResponseEntity.ok(body);
    }
}
