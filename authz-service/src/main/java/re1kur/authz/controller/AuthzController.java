package re1kur.authz.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import re1kur.authz.service.AuthzService;
import re1kur.core.dto.JwtToken;
import re1kur.core.payload.LoginRequest;

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

}
