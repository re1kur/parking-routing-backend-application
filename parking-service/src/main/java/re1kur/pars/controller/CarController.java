package re1kur.pars.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import re1kur.core.dto.CarDto;
import re1kur.core.dto.CarFullDto;
import re1kur.core.dto.CarShortDto;
import re1kur.core.payload.CarPayload;
import re1kur.core.payload.CarUpdatePayload;
import re1kur.pars.service.CarService;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/cars")
@RequiredArgsConstructor
public class CarController {
    private final CarService carService;

    @PostMapping("/register")
    public ResponseEntity<CarShortDto> registerCar(
            @RequestHeader(name = "Authorization") String token,
            @RequestBody @Valid CarPayload payload
    ) {
        CarShortDto body = carService.register(payload, token);
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    @PutMapping("/edit")
    public ResponseEntity<CarFullDto> editCar(
            @RequestHeader("Authorization") String token,
            @RequestBody @Valid CarUpdatePayload payload
    ) {
        CarFullDto updated = carService.edit(payload, token);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/get-short")
    public ResponseEntity<CarShortDto> getShortCar(
            @RequestParam(name = "id") UUID id
    ) {
        CarShortDto body = carService.getShort(id);
        return ResponseEntity.ok(body);
    }

    @GetMapping("/get-full")
    public ResponseEntity<CarFullDto> getFullCar(
            @RequestParam(name = "id") UUID id
    ) {
        CarFullDto body = carService.getFull(id);
        return ResponseEntity.ok(body);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteCar(
            @RequestParam(name = "id") UUID id,
            @RequestHeader(name = "Authorization") String token
    ) {
        carService.delete(id, token);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/my-list")
    public ResponseEntity<List<CarDto>> getMyCars(
            @RequestHeader(name = "Authorization") String token
    ) {
        List<CarDto> list = carService.getCarsByToken(token);
        return ResponseEntity.ok(list);
    }
}
