package re1kur.pars.controller.place;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import re1kur.core.dto.PlaceDto;
import re1kur.pars.service.parking.PlaceService;

import java.util.List;

@RestController
@RequestMapping("/api/places/{id}/available")
@RequiredArgsConstructor
public class AvailablePlaceController {
    private final PlaceService service;

    @GetMapping("/list/now")
    public ResponseEntity<List<PlaceDto>> availableListByTodayForNumber(
            @PathVariable(name = "id") Integer number
    ) {
        List<PlaceDto> body = service.getAvailableListNowByNumber(number);
        return ResponseEntity.ok(body);
    }

    @GetMapping("/list/{date}")
    public ResponseEntity<List<PlaceDto>> availableListByDate(
            @PathVariable(name = "id") Integer number,
            @PathVariable(name = "date") String date
    ) {
        List<PlaceDto> body = service.getAvailableListByNumberAndDate(date);
        return ResponseEntity.ok(body);
    }
}
