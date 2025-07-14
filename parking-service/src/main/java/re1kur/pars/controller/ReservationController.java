package re1kur.pars.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import re1kur.core.dto.ReservationShortDto;

import java.util.UUID;

@RestController
@RequestMapping("/api/reservations/{id}")
@RequiredArgsConstructor
public class ReservationController {
    // todo: policy
    @GetMapping("/short")
    public ResponseEntity<ReservationShortDto> getShortDto(
            @PathVariable(name = "id") UUID id
    ) {
        return ResponseEntity.ok(null);
    }
}
