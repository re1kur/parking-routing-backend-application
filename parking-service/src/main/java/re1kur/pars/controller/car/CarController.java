package re1kur.pars.controller.car;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import re1kur.core.dto.CarFullDto;
import re1kur.core.dto.CarShortDto;
import re1kur.core.payload.CarUpdatePayload;
import re1kur.pars.service.CarService;

import java.util.UUID;

@RestController
@RequestMapping("/api/cars/{id}")
@RequiredArgsConstructor
public class CarController {
    private final CarService carService;

    @PutMapping("/update")
    public ResponseEntity<CarFullDto> update(
            @RequestHeader("Authorization") String token,
            @PathVariable(name = "id") UUID id,
            @RequestBody @Valid CarUpdatePayload payload
    ) {
        CarFullDto updated = carService.update(id, payload, token);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/short")
    public ResponseEntity<CarShortDto> getShortCar(
            @PathVariable(name = "id") UUID id
    ) {
        CarShortDto body = carService.getShort(id);
        return ResponseEntity.ok(body);
    }

    @GetMapping("/full")
    public ResponseEntity<CarFullDto> getFullCar(
            @PathVariable(name = "id") UUID id
    ) {
        CarFullDto body = carService.getFull(id);
        return ResponseEntity.ok(body);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteCar(
            @PathVariable(name = "id") UUID id,
            @RequestHeader(name = "Authorization") String token
    ) {
        carService.delete(id, token);
        return ResponseEntity.noContent().build();
    }
}
