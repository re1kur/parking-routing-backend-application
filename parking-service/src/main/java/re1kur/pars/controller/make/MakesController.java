package re1kur.pars.controller.make;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import re1kur.core.dto.MakeDto;
import re1kur.core.payload.MakePayload;
import re1kur.pars.service.MakeService;

import java.util.List;

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

    // TODO: add to privacy
    @GetMapping("/list")
    public ResponseEntity<List<MakeDto>> getList(
        @RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
        @RequestParam(name = "size", required = false, defaultValue = "10") Integer size
    ) {
        List<MakeDto> body = service.getPage(page, size);
        return ResponseEntity.ok(body);
    }
}
