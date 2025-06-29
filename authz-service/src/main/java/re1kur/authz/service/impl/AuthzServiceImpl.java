package re1kur.authz.service.impl;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import re1kur.authz.client.IdentityClient;
import re1kur.core.dto.Credentials;
import re1kur.authz.jwt.JwtProvider;
import re1kur.authz.service.AuthzService;
import re1kur.core.dto.JwtToken;
import re1kur.core.payload.LoginRequest;

@Service
@RequiredArgsConstructor
public class AuthzServiceImpl implements AuthzService {
    private final IdentityClient idClient;
    private final JwtProvider jwtProvider;

    @Override
    public JwtToken login(@Valid LoginRequest payload) {
        Credentials credentials = idClient.authenticate(payload);
        return jwtProvider.getToken(credentials);
    }
}
