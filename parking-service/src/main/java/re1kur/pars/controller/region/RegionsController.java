package re1kur.pars.controller.region;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import re1kur.core.dto.PageDto;
import re1kur.core.dto.RegionDto;
import re1kur.core.payload.RegionPayload;
import re1kur.pars.service.other.RegionService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/regions")
public class RegionsController {
    private final RegionService regionService;

    @PostMapping("/create")
    public ResponseEntity<RegionDto> create(
            @RequestHeader(name = "Authorization") String bearer,
            @RequestBody @Valid RegionPayload payload
    ) {
        RegionDto body = regionService.create(payload, bearer);
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    @GetMapping("/list")
    public ResponseEntity<PageDto<RegionDto>> getList(
            @RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(name = "size", required = false, defaultValue = "10") Integer size
    ) {
        PageDto<RegionDto> body = regionService.getPage(page, size);
        return ResponseEntity.ok(body);
    }
}
