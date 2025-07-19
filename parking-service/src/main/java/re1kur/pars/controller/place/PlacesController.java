package re1kur.pars.controller.place;

import jakarta.validation.Valid;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import re1kur.core.dto.PlaceDto;
import re1kur.core.payload.PlacePayload;
import re1kur.pars.service.parking.PlaceService;

import java.util.List;

@RestController
@RequestMapping("/api/places")
@RequiredArgsConstructor
public class PlacesController {
    private final PlaceService service;

    @PostMapping("/create")
    public ResponseEntity<PlaceDto> create(
            @RequestHeader(name = "Authorization") String bearer,
            @RequestBody @Valid PlacePayload payload
    ) {
        PlaceDto body = service.create(payload, bearer);
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }


    @GetMapping("/list")
    public ResponseEntity<List<PlaceDto>> getList(
            @RequestParam(name = "page", required = false, defaultValue = "0") @PositiveOrZero Integer page,
            @RequestParam(name = "size", required = false, defaultValue = "10") Integer size
    ) {
        List<PlaceDto> body = service.getPage(page, size);

        return ResponseEntity.ok(body);
    }
}
