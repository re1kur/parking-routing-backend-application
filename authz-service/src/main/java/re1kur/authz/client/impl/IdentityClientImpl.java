package re1kur.authz.client.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import re1kur.authz.client.IdentityClient;
import re1kur.core.dto.Credentials;
import re1kur.core.exception.IdentityServiceAuthenticationException;
import re1kur.core.payload.LoginRequest;

@Component
@RequiredArgsConstructor
public class IdentityClientImpl implements IdentityClient {
    private final RestTemplate template;

    @Value("${client.identity-service.login-endpoint}")
    private String loginEndpoint;


    @Override
    public Credentials authenticate(LoginRequest request) {
        try {
            ResponseEntity<Credentials> response = template.exchange(
                    loginEndpoint, HttpMethod.POST, new HttpEntity<>(request), Credentials.class);

            return response.getBody();
        } catch (HttpStatusCodeException ex) {
            String body = ex.getResponseBodyAsString();
            HttpStatusCode statusCode = ex.getStatusCode();
            throw new IdentityServiceAuthenticationException(body, statusCode.value());
        }
    }
}
