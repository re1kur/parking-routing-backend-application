package re1kur.authz.service;

import re1kur.core.dto.JwtToken;
import re1kur.core.event.ServiceRegisteredEvent;
import re1kur.core.payload.LoginRequest;

public interface AuthzService {
    JwtToken login(LoginRequest payload);

    void authorizeRequest(String token, String uri, String method);

    void registerPrivacyPolicy(ServiceRegisteredEvent event);
}
