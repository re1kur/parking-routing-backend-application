package re1kur.pars.controller.car;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import re1kur.core.dto.CarImageDto;
import re1kur.core.payload.CarImagePayload;
import re1kur.pars.service.car.CarImageService;

import java.util.UUID;

@RestController
@RequestMapping("/api/cars/{id}/images")
@RequiredArgsConstructor
public class CarImageController {
    private final CarImageService imgService;

    // TODO: POLICY!!!

    @PostMapping("/create")
    public ResponseEntity<CarImageDto> attachCarImage(
            @RequestHeader(name = "Authorization") String bearer,
            @RequestBody @Valid CarImagePayload payload,
            @PathVariable(name = "id") UUID carId
    ) {
        CarImageDto body = imgService.create(carId, payload, bearer);
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    @DeleteMapping("/{file_id}/delete")
    public ResponseEntity<Void> deleteCarImage(
            @RequestHeader(name = "Authorization") String bearer,
            @PathVariable(name = "id") UUID carId,
            @PathVariable(name = "file_id") String fileId
    ) {
        imgService.delete(carId, fileId, bearer);
        return ResponseEntity.noContent().build();
    }
}
