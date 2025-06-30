package re1kur.is.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import re1kur.core.dto.UserDto;
import re1kur.core.payload.UserInformationPayload;
import re1kur.is.service.UserService;
import org.springframework.security.oauth2.jwt.Jwt;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserInformationController {
    private final UserService service;

    @GetMapping("/info")
    public ResponseEntity<UserDto> getInfo(@RequestHeader("Authorization") String authHeader) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(service.getPersonalInfo(authHeader));
    }

    @PutMapping("/info/update")
    public ResponseEntity<UserDto> updateInfo(@AuthenticationPrincipal Jwt token,
                                              @RequestBody UserInformationPayload payload) {
        UserDto body = service.updateUserInfo(payload, token.getSubject());
        return ResponseEntity.ok(body);
    }
}
