package re1kur.authz.service;

import re1kur.core.dto.JwtToken;
import re1kur.core.payload.LoginRequest;

import java.text.ParseException;

public interface AuthzService {
    JwtToken login(LoginRequest payload);

    void authorizeRequest(String token, String uri, String method) throws ParseException;
}
