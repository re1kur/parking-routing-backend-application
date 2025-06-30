package re1kur.authz.client;

import re1kur.authz.authz.ParsedURI;

import java.util.List;
import java.util.Optional;

public interface JdbcClient {
    Optional<List<String>> findRolesForEndpoint(ParsedURI parsedURI, String methodType);
}
