package re1kur.authz.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import re1kur.authz.service.AuthzService;
import re1kur.core.dto.JwtToken;
import re1kur.core.payload.LoginRequest;

import java.text.ParseException;

@Slf4j
@RestController
@RequestMapping("/api/authz")
@RequiredArgsConstructor
public class AuthzController {
    private final AuthzService authzService;

    @PostMapping("login")
    private ResponseEntity<JwtToken> login(@RequestBody @Valid LoginRequest request) {
        log.info("Login request: {}", request.toString());
        JwtToken token = authzService.login(request);
        return ResponseEntity.ok(token);
    }

    @PostMapping("authorize")
    private ResponseEntity<Void> authorizeRequest(
            @RequestHeader("Authorization") String authHeader,
            @RequestHeader(name = "X-Original-URI") String uri,
            @RequestHeader(name = "X-Original-Method") String method) throws ParseException {
        authzService.authorizeRequest(authHeader, uri, method);
        return ResponseEntity.ok().build();
    }

}
