package re1kur.pars.controller.region;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import re1kur.core.dto.RegionDto;
import re1kur.core.payload.RegionPayload;
import re1kur.pars.service.other.RegionService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/regions/{id}")
public class RegionController {
    private final RegionService service;

    @GetMapping
    public ResponseEntity<RegionDto> get(
            @PathVariable(name = "id") Integer regionId
    ) {
        RegionDto body = service.get(regionId);
        return ResponseEntity.ok(body);
    }

    @PutMapping("/update")
    public ResponseEntity<RegionDto> update(
            @PathVariable(name = "id") Integer regionId,
            @RequestBody @Valid RegionPayload payload,
            @RequestHeader(name = "Authorization") String bearer
    ) {
        RegionDto body = service.update(payload, regionId, bearer);
        return ResponseEntity.ok(body);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> delete(
            @PathVariable(name = "id") Integer regionId,
            @RequestHeader(name = "Authorization") String bearer
    ) {
        service.delete(regionId, bearer);
        return ResponseEntity.noContent().build();
    }
}
