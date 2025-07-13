package re1kur.authz.client.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import re1kur.core.other.EndpointAccessRule;
import re1kur.authz.client.JdbcClient;
import re1kur.core.dto.PrivacyPolicy;
import re1kur.core.event.ServiceRegisteredEvent;

import java.sql.PreparedStatement;
import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class JdbcClientImpl implements JdbcClient {
    public static final String DELETE_SERVICE_SQL_QUERY = "DELETE FROM services WHERE id = ?";
    public static final String SELECT_SERVICE_SQL_QUERY = "SELECT id FROM services WHERE name = ?";
    private final JdbcTemplate template;

    private static final String SELECT_ENDPOINT_ROLES_SQL_QUERY = """
            SELECT access.role
              FROM services s
              JOIN endpoints e ON e.service_id = s.id
              JOIN access_rules access ON access.endpoint_id = e.id
             WHERE s.name = ?
               AND ? LIKE CONCAT(e.method_name, '%')
               AND e.method_type = ?;
            """;

    private static final String INSERT_NEW_SERVICE_SQL_QUERY = """
            INSERT INTO services (name, api_prefix) VALUES (?, ?)
            ON CONFLICT DO NOTHING
            RETURNING id;
            """;

    private static final String INSERT_NEW_ENDPOINT_SQL_QUERY = """
            INSERT INTO endpoints (service_id, method_name, method_type)
            VALUES (?, ?, ?)
            ON CONFLICT DO NOTHING
            RETURNING id;
            """;

    private static final String INSERT_PUBLIC_ACCESS_RULE_SQL_QUERY = """
            INSERT INTO access_rules (endpoint_id) VALUES (?)
            ON CONFLICT DO NOTHING;
            """;
    private static final String INSERT_PRIVATE_ACCESS_RULE_SQL_QUERY = """
            INSERT INTO access_rules (endpoint_id, role) VALUES (?, ?)
            ON CONFLICT DO NOTHING;
            """;

    private static final String SELECT_ENDPOINTS_FOR_METHOD_TYPE_SQL_QUERY = """
             SELECT e.method_name, a.role\s
               FROM endpoints e
               JOIN services s ON s.id = e.service_id
               JOIN access_rules a ON a.endpoint_id = e.id
              WHERE s.name = ?
                AND e.method_type = ?;
            \s""";

    private static final String SELECT_ENDPOINT_FOR_HOST_AND_NAME_AND_TYPE = """
            SELECT e.method_name,
                   a.role
              FROM services s
              JOIN endpoints e ON e.service_id = s.id
              JOIN access_rules a ON a.endpoint_id = e.id
             WHERE s.name = ?
               AND ? LIKE CONCAT(e.method_name, '%')
               AND e.method_type = ?;
            """;
    private static final String FIND_SERVICE_API_PREFIX_FOR_SERVICE_NAME_SQL_QUERY = """
            SELECT s.api_prefix FROM services s WHERE s.name = ?;
            """;

    @Override
    public Optional<List<String>> findRolesForEndpoint(String service, String endpoint, String type) {
        log.info("service(host): {}, method: {}", service, endpoint);
        List<String> roles = template.queryForList(
                SELECT_ENDPOINT_ROLES_SQL_QUERY,
                String.class,
                service,
                endpoint,
                type);
        log.info("roles [{}] for endpoint {}", roles, endpoint);
        return roles.isEmpty() ? Optional.empty() : Optional.of(roles);
    }

    @Override
    public Optional<EndpointAccessRule> findEndpoint(String service, String endpoint, String type) {
        log.info("JDBC FIND ENDPOINT BY service-host: {}, method: {}, method type {}", service, endpoint, type);
        List<Map<String, Object>> maps = template.queryForList(
                SELECT_ENDPOINT_FOR_HOST_AND_NAME_AND_TYPE,
                service,
                endpoint,
                type);

        EndpointAccessRule rule = mapEndpoint(maps);

        log.info("JDBC Found: {}", rule == null ? "NULL" : rule);
        return Optional.ofNullable(rule);
    }

    private EndpointAccessRule mapEndpoint(List<Map<String, Object>> maps) {
        if (maps == null || maps.isEmpty()) {
            return null;
        }
        EndpointAccessRule rule = null;
        for (Map<String, Object> map : maps) {
            String methodName = (String) map.get("method_name");
            String role = (String) map.get("role");

            if (rule == null) {
                rule = new EndpointAccessRule(methodName);
            }

            if (role != null && !role.isEmpty()) {
                rule.addRole(role);
            }
        }
        return rule;
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
        Integer serviceId = insertService(event.serviceName(), event.apiPrefix());

        log.info("Service �{}: {}", serviceId, event.serviceName());

        for (PrivacyPolicy policy : event.policies()) {
            log.info("Current policy: {}", policy.toString());
            Integer endpointId = insertEndpoint(policy, serviceId);
            log.info("Inserted endpoint: {} [{}]", policy.methodName(), policy.methodType());

            if (policy.roles().isEmpty()) {
                insertPublicAccessRule(endpointId);
                log.info("Inserted public policy: {}", policy);
                continue;
            }
            insertPrivateAccessRules(policy.roles(), endpointId);
            log.info("Inserted private policy: {}", policy);
        }
    }

    @Override
    public List<EndpointAccessRule> findEndpointsForMethod(String service, String methodType) {
        List<Map<String, Object>> maps = template
                .queryForList(SELECT_ENDPOINTS_FOR_METHOD_TYPE_SQL_QUERY, service, methodType);
        return mapEndpoints(maps);
    }

    @Override
    public String findApiPrefixByServiceName(String service) {
        return template.queryForObject(FIND_SERVICE_API_PREFIX_FOR_SERVICE_NAME_SQL_QUERY,
                String.class,
                service);
    }

    private List<EndpointAccessRule> mapEndpoints(List<Map<String, Object>> maps) {
        if (maps.isEmpty())
            return List.of();
        Map<String, EndpointAccessRule> grouped = new HashMap<>();
        for (Map<String, Object> map : maps) {
            String methodName = (String) map.get("method_name");
            String role = (String) map.get("role");

            EndpointAccessRule rule = grouped.get(methodName);
            if (rule == null) {
                rule = new EndpointAccessRule(methodName);
                grouped.put(methodName, rule);
            }
            if (role != null && !role.isEmpty())
                rule.addRole(role);
        }

        return grouped.values().stream().toList();
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

    private Integer insertService(String serviceName, String apiPrefix) {
        return template.query(con -> {
            PreparedStatement ps = con.prepareStatement(INSERT_NEW_SERVICE_SQL_QUERY);
            ps.setString(1, serviceName);
            ps.setString(2, apiPrefix);
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
        return ids.isEmpty() ? null : ids.getFirst();
    }
}
