package re1kur.pars.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import re1kur.core.dto.ParkingPlaceReservationDto;
import re1kur.core.dto.PlaceReservationsDto;
import re1kur.core.payload.ParkingPlaceReservationPayload;
import re1kur.pars.service.ReservationService;

import java.time.OffsetDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationController {
    private final ReservationService service;

    @PostMapping("create")
    public ResponseEntity<ParkingPlaceReservationDto> createReservation(
            @RequestHeader(name = "Authorization") String token,
            @RequestBody @Valid ParkingPlaceReservationPayload payload
    ) {
        ParkingPlaceReservationDto body = service.create(token, payload);
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    @GetMapping("/my-list")
    public ResponseEntity<Page<ParkingPlaceReservationDto>> getMyReservations(
            @RequestHeader(name = "Authorization") String token,
            @RequestParam(name = "page", defaultValue = "1") Integer page,
            @RequestParam(name = "size", defaultValue = "10") Integer size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ParkingPlaceReservationDto> body = service.getPageByUserId(token, pageable);
        return ResponseEntity.ok(body);
    }

    @GetMapping("/my-list/{date}")
    public ResponseEntity<Page<ParkingPlaceReservationDto>> getMyReservationsByDate(
            @RequestHeader(name = "Authorization") String token,
            @PathVariable(name = "date") String date,
            @RequestParam(name = "page", defaultValue = "1") Integer page,
            @RequestParam(name = "size", defaultValue = "10") Integer size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        OffsetDateTime offsetDateTime = OffsetDateTime.parse(date);
        Page<ParkingPlaceReservationDto> body = service.getPageByUserIdAndDate(token, offsetDateTime, pageable);
        return ResponseEntity.ok(body);
    }

    @GetMapping("/all-list/today")
    public ResponseEntity<List<PlaceReservationsDto>> getListReservationsForToday(
    ) {
        List<PlaceReservationsDto> body = service.getListForToday();
        return ResponseEntity.ok(body);
    }

    @GetMapping(params = "number", path = "/list/today")
    public ResponseEntity<List<ParkingPlaceReservationDto>> getListReservationsByNumberForToday(
            @RequestParam(name = "number") Integer number
    ) {
        List<ParkingPlaceReservationDto> body = service.getListByPlaceNumberForToday(number);
        return ResponseEntity.ok(body);
    }

    @GetMapping("/list/{date}")
    public ResponseEntity<List<PlaceReservationsDto>> getListReservationsByDate(
            @PathVariable(name = "date") String date
    ) {
        OffsetDateTime offsetDateTime = OffsetDateTime.parse(date);
        List<PlaceReservationsDto> body = service.getListForDate(offsetDateTime);
        return ResponseEntity.ok(body);
    }
}
