package re1kur.authz.client.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import re1kur.authz.authz.ParsedURI;
import re1kur.authz.client.JdbcClient;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JdbcClientImpl implements JdbcClient {
    private final JdbcTemplate template;

    private String SELECT_ENDPOINT_ROLES_SQL_QUERY = """
            SELECT access.role
                  FROM services s
                  JOIN endpoints e ON e.service_id = s.id
                  JOIN access_rules access ON access.endpoint_id = e.id
                 WHERE s.name = ?
                   AND e.method_name = ?
                   AND e.method_type = ?;
            """;

    @Override
    public Optional<List<String>> findRolesForEndpoint(ParsedURI parsedURI, String methodType) {
        List<String> roles = template.queryForList(
                SELECT_ENDPOINT_ROLES_SQL_QUERY,
                String.class,
                parsedURI.getService(),
                parsedURI.getEndpoint(),
                methodType);

        return roles.isEmpty() ? Optional.empty() : Optional.of(roles);
    }

}
