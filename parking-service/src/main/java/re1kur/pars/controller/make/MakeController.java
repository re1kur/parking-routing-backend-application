package re1kur.pars.controller.make;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import re1kur.core.dto.MakeDto;
import re1kur.core.payload.MakePayload;
import re1kur.pars.service.other.MakeService;

@RestController
@RequestMapping("/api/makes/{id}")
@RequiredArgsConstructor
public class MakeController {
    private final MakeService service;

    @GetMapping
    public ResponseEntity<MakeDto> get(
            @PathVariable(name = "id") Integer makeId
    ) {
        MakeDto body = service.get(makeId);
        return ResponseEntity.ok(body);
    }

    @PutMapping("/update")
    public ResponseEntity<MakeDto> update(
            @PathVariable(name = "id") Integer makeId,
            @RequestBody @Valid MakePayload payload,
            @RequestHeader(name = "Authorization") String bearer
    ) {
        MakeDto body = service.update(payload, makeId, bearer);
        return ResponseEntity.ok(body);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> delete(
            @PathVariable(name = "id") Integer makeId,
            @RequestHeader(name = "Authorization") String bearer
    ) {
        service.delete(makeId, bearer);
        return ResponseEntity.noContent().build();
    }
}
