package re1kur.authz.service;


import re1kur.core.dto.JwtToken;

import java.util.Map;

public interface TokenService {
    JwtToken refreshToken(String refreshToken);

    Map<String, Object> getPublicKey() throws Exception;
}
