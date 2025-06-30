package re1kur.authz.client.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import re1kur.core.other.ParsedURI;
import re1kur.authz.client.JdbcClient;
import re1kur.core.dto.PrivacyPolicy;
import re1kur.core.event.ServiceRegisteredEvent;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class JdbcClientImpl implements JdbcClient {
    public static final String DELETE_SERVICE_SQL_QUERY = "DELETE FROM services WHERE id = ?";
    public static final String SELECT_SERVICE_SQL_QUERY = "SELECT id FROM services WHERE name = ?";
    private final JdbcTemplate template;

    private static String SELECT_ENDPOINT_ROLES_SQL_QUERY = """
            SELECT access.role
                  FROM services s
                  JOIN endpoints e ON e.service_id = s.id
                  JOIN access_rules access ON access.endpoint_id = e.id
                 WHERE s.name = ?
                   AND e.method_name = ?
                   AND e.method_type = ?;
            """;
    private static String INSERT_NEW_SERVICE_SQL_QUERY = """
            INSERT INTO services (name) VALUES (?)
            ON CONFLICT DO NOTHING
            RETURNING id;
            """;

    private static String INSERT_NEW_ENDPOINT_SQL_QUERY = """
            INSERT INTO endpoints (service_id, method_name, method_type)
            VALUES (?, ?, ?)
            ON CONFLICT DO NOTHING
            RETURNING id;
            """;

    private static String INSERT_PUBLIC_ACCESS_RULE_SQL_QUERY = """
            INSERT INTO access_rules (endpoint_id) VALUES (?)
            ON CONFLICT DO NOTHING;
            """;
    private static String INSERT_PRIVATE_ACCESS_RULE_SQL_QUERY = """
            INSERT INTO access_rules (endpoint_id, role) VALUES (?, ?)
            ON CONFLICT DO NOTHING;
            """;

    private static String SELECT_SERVICE_ID_SQL_QUERY = """
            SELECT id FROM services WHERE name = ?;
            """;

    @Override
    public Optional<List<String>> findRolesForEndpoint(ParsedURI parsedURI, String methodType) {
        log.info("service(host): {}, method: {}", parsedURI.getService(), parsedURI.getEndpoint());
        List<String> roles = template.queryForList(
                SELECT_ENDPOINT_ROLES_SQL_QUERY,
                String.class,
                parsedURI.getService(),
                parsedURI.getEndpoint(),
                methodType);
        log.info("roles [{}] for endpoint {}", roles, parsedURI);
        return roles.isEmpty() ? Optional.empty() : Optional.of(roles);
    }

    @Override
    @Transactional
    public void saveEndpointsByService(ServiceRegisteredEvent event) {
        log.info("JDBC saving-updating service privacy policies: {}", event.toString());

        Integer existingServiceId = findServiceIdByName(event.serviceName());
        if (existingServiceId != null) {
            log.info("Service already exists: {}", existingServiceId);
            deleteServiceById(existingServiceId);
        }

        log.info("Inserting service: {}", event.serviceName());
        Integer serviceId = insertService(event.serviceName());

        log.info("Service ¹{}: {}", serviceId, event.serviceName());

        for (PrivacyPolicy policy : event.policies()) {
            log.info("Current policy: {}", policy.toString());
            Integer endpointId = insertEndpoint(policy, serviceId);
            log.info("Inserted endpoint: {} [{}]", policy.methodName(), policy.methodType());

            if (policy.roles().isEmpty()) {
                insertPublicAccessRule(endpointId);
                log.info("Inserted public policy: {}", policy);
                return;
            }
            insertPrivateAccessRules(policy.roles(), endpointId);
            log.info("Inserted private policy: {}", policy);
        }
    }

    private void insertPrivateAccessRules(List<String> roles, int endpointId) {
        roles.forEach(role -> template.update(INSERT_PRIVATE_ACCESS_RULE_SQL_QUERY, endpointId, role));
    }

    private void insertPublicAccessRule(int endpointId) {
        template.update(con -> {
            PreparedStatement ps = con.prepareStatement(INSERT_PUBLIC_ACCESS_RULE_SQL_QUERY);
            ps.setInt(1, endpointId);
            return ps;
        });
    }

    private Integer insertEndpoint(PrivacyPolicy policy, int serviceId) {
        return template.query(con -> {
            PreparedStatement ps = con.prepareStatement(INSERT_NEW_ENDPOINT_SQL_QUERY);
            ps.setInt(1, serviceId);
            ps.setString(2, policy.methodName());
            ps.setString(3, policy.methodType());
            return ps;
        }, rs -> {
            if (rs.next()) return rs.getInt(1);
            return null;
        });
    }

    private Integer insertService(String serviceName) {
        return template.query(con -> {
            PreparedStatement ps = con.prepareStatement(INSERT_NEW_SERVICE_SQL_QUERY);
            ps.setString(1, serviceName);
            return ps;
        }, rs -> {
            if (rs.next()) return rs.getInt(1);
            return null;
        });
    }

    private void deleteServiceById(int serviceId) {
        template.update(DELETE_SERVICE_SQL_QUERY, serviceId);
        log.info("Service has deleted: {}", serviceId);
    }

    private Integer findServiceIdByName(String name) {
        List<Integer> ids = template.query(
                SELECT_SERVICE_SQL_QUERY,
                (rs, rowNum) -> rs.getInt("id"),
                name
        );
        return ids.isEmpty() ? null : ids.get(0);
    }
}
