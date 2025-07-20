package re1kur.pars.controller.car;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import re1kur.core.dto.CarDto;
import re1kur.core.dto.CarFullDto;
import re1kur.core.dto.CarShortDto;
import re1kur.core.payload.CarUpdatePayload;
import re1kur.pars.service.car.CarService;

import java.util.UUID;

@RestController
@RequestMapping("/api/cars/{id}")
@RequiredArgsConstructor
public class CarController {
    private final CarService carService;

    @GetMapping
    public ResponseEntity<CarDto> getCar(
            @RequestHeader("Authorization") String bearer,
            @PathVariable(name = "id") UUID id
    ) {
        CarDto body = carService.getById(id, bearer);
        return ResponseEntity.ok(body);
    }

    @GetMapping("/short")
    public ResponseEntity<CarShortDto> getShortCar(
            @RequestHeader("Authorization") String bearer,
            @PathVariable(name = "id") UUID id
    ) {
        CarShortDto body = carService.getShort(id, bearer);
        return ResponseEntity.ok(body);
    }

    @GetMapping("/full")
    public ResponseEntity<CarFullDto> getFullCar(
            @RequestHeader("Authorization") String bearer,
            @PathVariable(name = "id") UUID id
    ) {
        CarFullDto body = carService.getFull(id, bearer);
        return ResponseEntity.ok(body);
    }

    @PutMapping("/update")
    public ResponseEntity<CarFullDto> update(
            @RequestHeader("Authorization") String token,
            @PathVariable(name = "id") UUID id,
            @RequestBody @Valid CarUpdatePayload payload
    ) {
        CarFullDto updated = carService.update(id, payload, token);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteCar(
            @PathVariable(name = "id") UUID id,
            @RequestHeader(name = "Authorization") String bearer
    ) {
        carService.delete(id, bearer);
        return ResponseEntity.noContent().build();
    }
}
