package re1kur.pars.controller.place;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import re1kur.pars.service.parking.PlaceService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/places/available")
@RequiredArgsConstructor
public class AvailablePlacesController {
    private final PlaceService service;

    @GetMapping("/list/now")
    public ResponseEntity<List<Integer>> availableList() {
        List<Integer> body = service.getAvailablePlacesByNow();
        // todo: reimagine returning body response
        return ResponseEntity.ok(body);
    }

    @GetMapping("/list/{date}")
    public ResponseEntity<List<Integer>> availableListByDate(
            @PathVariable(name = "date") LocalDate date
    ) {
        List<Integer> body = service.getAvailablePlacesByDate(date);
        return ResponseEntity.ok(body);
    }

    @GetMapping("/list")
    public ResponseEntity<List<Integer>> availableListByStartAndEnd(
            @RequestParam(name = "start") LocalDateTime startAt,
            @RequestParam(name = "end") LocalDateTime endAt
            ) {
        List<Integer> body = service.getAvailablePlacesByStartAndEnd(startAt, endAt);
        return ResponseEntity.ok(body);
    }
}
