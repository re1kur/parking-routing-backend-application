package re1kur.pars.client.impl;

import com.sun.net.httpserver.HttpsParameters;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import re1kur.pars.client.IdentityClient;

import java.net.URI;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class IdentityClientImpl implements IdentityClient {
    private final RestTemplate template;

    @Value("${client.identity-service.identify-endpoint}")
    private String identifyEndpoint;

    @Override
    public boolean isExistsById(UUID ownerId) {
        ResponseEntity<Void> response = template.exchange(
                URI.create(identifyEndpoint + "?id=" + ownerId),
                HttpMethod.GET,
                null,
                Void.class);
        return response.getStatusCode().is2xxSuccessful();
    }
}
