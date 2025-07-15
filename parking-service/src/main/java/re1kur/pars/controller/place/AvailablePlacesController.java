package re1kur.pars.controller.place;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import re1kur.core.dto.ParkingPlaceDto;
import re1kur.pars.service.parking.PlaceService;

import java.util.List;

@RestController
@RequestMapping("/api/places/available")
@RequiredArgsConstructor
public class AvailablePlacesController {
    private final PlaceService service;

    @GetMapping("/list")
    public ResponseEntity<List<ParkingPlaceDto>> availableList() {
        return null;
    }
}
