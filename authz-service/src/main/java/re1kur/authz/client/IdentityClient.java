package re1kur.authz.client;

import re1kur.core.dto.Credentials;
import re1kur.core.payload.LoginRequest;

public interface IdentityClient {
    Credentials authenticate(LoginRequest request);
}
