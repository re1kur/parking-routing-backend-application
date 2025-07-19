package re1kur.pars.controller.region;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import re1kur.core.dto.RegionCodeDto;
import re1kur.core.payload.RegionCodePayload;
import re1kur.pars.service.other.RegionService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/regions/{id}/codes")
public class RegionCodesController {
    private final RegionService service;

    @PostMapping("/create")
    public ResponseEntity<RegionCodeDto> createCode(
            @RequestHeader(name = "Authorization") String bearer,
            @PathVariable(name = "id") Integer id,
            @RequestBody @Valid RegionCodePayload payload
    ) {
        RegionCodeDto body = service.createCode(id, payload, bearer);
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

}
