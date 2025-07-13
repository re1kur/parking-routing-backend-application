package re1kur.authz.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import re1kur.authz.service.AuthzService;
import re1kur.core.dto.JwtToken;
import re1kur.core.payload.LoginRequest;

@RestController
@RequestMapping("/api/authz")
@RequiredArgsConstructor
public class AuthzController {
    private final AuthzService authzService;

    @PostMapping("/login")
    private ResponseEntity<JwtToken> login(
            @RequestBody @Valid LoginRequest request
    ) {
        JwtToken token = authzService.login(request);
        return ResponseEntity.ok(token);
    }

    @GetMapping("/authorize")
    private ResponseEntity<Void> authorizeRequest(
            @RequestHeader(name = "Authorization") String authHeader,
            @RequestHeader(name = "X-Original-Service") String service,
            @RequestHeader(name = "X-Original-URI") String uri,
            @RequestHeader(name = "X-Original-Method") String method
    ) {
        authzService.authorizeRequest(authHeader, uri, method, service);
        return ResponseEntity.ok().build();
    }
}
