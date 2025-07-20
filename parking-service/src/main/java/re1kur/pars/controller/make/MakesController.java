package re1kur.pars.controller.make;

import jakarta.validation.Valid;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import re1kur.core.dto.MakeDto;
import re1kur.core.dto.PageDto;
import re1kur.core.payload.MakePayload;
import re1kur.pars.service.other.MakeService;

@RestController
@RequestMapping("/api/makes")
@RequiredArgsConstructor
public class MakesController {
    private final MakeService service;

    @PostMapping("/create")
    public ResponseEntity<MakeDto> create(
            @RequestBody @Valid MakePayload payload,
            @RequestHeader(name = "Authorization") String bearer
    ) {
        MakeDto body = service.create(payload, bearer);
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    @GetMapping("/list")
    public ResponseEntity<PageDto<MakeDto>> getList(
            @RequestParam(name = "page", required = false, defaultValue = "0") @PositiveOrZero Integer page,
            @RequestParam(name = "size", required = false, defaultValue = "10") Integer size
    ) {
        PageDto<MakeDto> body = service.getPage(page, size);
        return ResponseEntity.ok(body);
    }
}
