package re1kur.authz.client;

import re1kur.core.other.ParsedURI;
import re1kur.core.event.ServiceRegisteredEvent;

import java.util.List;
import java.util.Optional;

public interface JdbcClient {
    Optional<List<String>> findRolesForEndpoint(ParsedURI parsedURI, String methodType);

    void saveEndpointsByService(ServiceRegisteredEvent event);
}
