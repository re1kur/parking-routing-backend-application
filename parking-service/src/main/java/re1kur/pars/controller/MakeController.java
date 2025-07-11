package re1kur.pars.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import re1kur.core.dto.MakeDto;
import re1kur.core.payload.MakePayload;
import re1kur.pars.service.MakeService;

@RestController
@RequestMapping("/api/makes")
@RequiredArgsConstructor
public class MakeController {
    private final MakeService service;

    @PostMapping("/create")
    public ResponseEntity<MakeDto> create(@RequestBody @Valid MakePayload payload) {
        MakeDto body = service.create(payload);
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    @GetMapping("/get")
    public ResponseEntity<MakeDto> get(@RequestParam(name = "id") Integer makeId) {
        MakeDto body = service.get(makeId);
        return ResponseEntity.ok(body);
    }

    @PutMapping("/{id}/update")
    public ResponseEntity<MakeDto> update(
            @PathVariable(name = "id") Integer makeId,
            @RequestBody @Valid MakePayload payload
    ) {
        MakeDto body = service.update(payload, makeId);
        return ResponseEntity.ok(body);
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<Void> delete(
            @PathVariable(name = "id") Integer makeId
    ) {
        service.delete(makeId);
        return ResponseEntity.noContent().build();
    }
}
