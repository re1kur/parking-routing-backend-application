package re1kur.authz.client;

import re1kur.core.other.EndpointAccessRule;
import re1kur.core.event.ServiceRegisteredEvent;

import java.util.List;
import java.util.Optional;

public interface JdbcClient {
    Optional<EndpointAccessRule> findEndpoint(String service, String endpoint, String type);

    Optional<List<String>> findRolesForEndpoint(String service, String endpoint, String type);

    void saveEndpointsByService(ServiceRegisteredEvent event);

    List<EndpointAccessRule> findEndpointsForMethod(String service, String methodType);

    String findApiPrefixByServiceName(String service);
}
