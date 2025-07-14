package re1kur.pars.controller.car;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import re1kur.core.dto.CarDto;
import re1kur.core.dto.CarShortDto;
import re1kur.core.payload.CarPayload;
import re1kur.pars.service.car.CarService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/cars")
@RequiredArgsConstructor
public class CarsController {
    private final CarService carService;

    @PostMapping("/create")
    public ResponseEntity<CarShortDto> create(
            @RequestHeader(name = "Authorization") String token,
            @RequestBody @Valid CarPayload payload
    ) {
        CarShortDto body = carService.create(payload, token);
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    // TODO: EDIT POLICY!
    @GetMapping("/list")
    public ResponseEntity<List<CarDto>> getMyCars(
            @RequestHeader(name = "Authorization") String bearer
    ) {
        List<CarDto> list = carService.getCarsByOwner(bearer);
        return ResponseEntity.ok(list);
    }
}
