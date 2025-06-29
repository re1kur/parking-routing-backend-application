package re1kur.authz.service;

import jakarta.validation.Valid;
import re1kur.core.dto.JwtToken;
import re1kur.core.payload.LoginRequest;

public interface AuthzService {
    JwtToken login(@Valid LoginRequest payload);
}
