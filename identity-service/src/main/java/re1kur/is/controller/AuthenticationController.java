package re1kur.is.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import re1kur.core.dto.Credentials;
import re1kur.core.payload.GenerateCodeRequest;
import re1kur.core.payload.LoginRequest;
import re1kur.core.payload.UserPayload;
import re1kur.is.service.AuthService;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthenticationController {
    private final AuthService service;

    @PostMapping("/register")
    public ResponseEntity<UUID> register(@RequestBody @Valid UserPayload payload) {
        UUID body = service.register(payload);
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    @PostMapping("/login")
    public ResponseEntity<Credentials> login(@RequestBody @Valid LoginRequest request) {
        Credentials cred = service.login(request);
        return ResponseEntity.ok(cred);
    }

    @PostMapping("/code")
    public ResponseEntity<Void> generateCode(@RequestBody @Valid GenerateCodeRequest request) {
        service.generateCode(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
