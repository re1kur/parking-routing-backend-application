package re1kur.authz.service.impl;

import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;
import re1kur.core.exception.InvalidTokenException;
import re1kur.core.other.EndpointAccessRule;
import re1kur.authz.client.IdentityClient;
import re1kur.authz.client.JdbcClient;
import re1kur.core.dto.Credentials;
import re1kur.authz.jwt.JwtProvider;
import re1kur.authz.service.AuthzService;
import re1kur.core.dto.JwtToken;
import re1kur.core.event.ServiceRegisteredEvent;
import re1kur.core.exception.EndpointNotFoundException;
import re1kur.core.exception.TokenDidNotPassVerificationException;
import re1kur.core.exception.UserDoesNotHavePermissionForEndpoint;
import re1kur.core.payload.LoginRequest;

import java.net.URI;
import java.text.ParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthzServiceImpl implements AuthzService {
    private final IdentityClient idClient;
    private final JwtProvider jwtProvider;
    private final JdbcClient jdbcClient;

    @Value("${jwt.claim-name.roles}")
    private String rolesClaimName;

    @Override
    public JwtToken login(LoginRequest request) {
        log.info("Login request: {}", request.toString());
        Credentials credentials = idClient.authenticate(request);
        return jwtProvider.getToken(credentials);
    }

    @Override
    public void authorizeRequest(String token, String uri, String methodType, String service) {
        log.info("AUTHORIZE URI: {}", uri);
        AntPathMatcher matcher = new AntPathMatcher();
        List<String> allowedRoles;
        String endpoint = URI.create(uri).getPath();

        log.info("Authorize request: Token [{}] TO {}{} [{}]", token, service, endpoint, methodType);
        String prefix = jdbcClient.findApiPrefixByServiceName(service);
        if (endpoint.startsWith(prefix)) {
            endpoint = endpoint.replaceFirst(prefix, "/api");
        }
        log.info("REPLACED ENDPOINT: {}", endpoint);

        Optional<EndpointAccessRule> optionalEndpoint = jdbcClient.findEndpoint(service, endpoint, methodType);
        if (optionalEndpoint.isPresent()) {
            EndpointAccessRule rule = optionalEndpoint.get();
            allowedRoles = rule.getRoles();
            authorizeRoles(uri, methodType, allowedRoles, token);
            return;
        }

        List<EndpointAccessRule> rules = jdbcClient.findEndpointsForMethod(service, methodType);

        for (EndpointAccessRule rule : rules) {
            if (matcher.match(rule.getMethodName(), endpoint)) {
                log.info("MATCH: {}", rule);
                allowedRoles = rule.getRoles();
                authorizeRoles(uri, methodType, allowedRoles, token);
                return;
            }
        }

        throw new EndpointNotFoundException("Endpoint %s [%s] not found.".formatted(uri, methodType));
    }

    private void authorizeRoles(String uri, String methodType, List<String> allowedRoles, String token) {
        if (allowedRoles.isEmpty())
            return;

        JWT jwt = getBearer(token);

        String subject = getJwtClaimsSet(jwt).getSubject();
        if (!jwtProvider.verifySignature(jwt))
            throw new TokenDidNotPassVerificationException("Token did not pass verification.");

        String rolesClaim = getRolesClaim(jwt);
        if (rolesClaim == null)
            throw new UserDoesNotHavePermissionForEndpoint
                    ("User '%s' does not have permission for endpoint '%s' [%s]."
                            .formatted(subject, uri, methodType));

        List<String> tokenRoles = Arrays.stream(rolesClaim.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .toList();

        boolean hasAccess = tokenRoles.stream().anyMatch(allowedRoles::contains);
        if (!hasAccess) {
            throw new UserDoesNotHavePermissionForEndpoint
                    ("User '%s' does not have permission for endpoint '%s' [%s]."
                            .formatted(subject, uri, methodType));
        }
    }

    private String getRolesClaim(JWT jwt) {
        try {
            return getJwtClaimsSet(jwt).getStringClaim(rolesClaimName);
        } catch (ParseException e) {
            throw new InvalidTokenException("Invalid token: %s".formatted(jwt.serialize()));
        }
    }

    private static JWTClaimsSet getJwtClaimsSet(JWT jwt) {
        try {
            return jwt.getJWTClaimsSet();
        } catch (ParseException e) {
            throw new InvalidTokenException("Invalid token: %s".formatted(jwt.serialize()));
        }
    }

    private static SignedJWT getBearer(String token) {
        if (token == null) {
            throw new UserDoesNotHavePermissionForEndpoint("This endpoint requires header 'Authorization'.");
        }
        try {
            return SignedJWT.parse(token.replace("Bearer ", ""));
        } catch (ParseException e) {
            throw new InvalidTokenException("Invalid token: %s".formatted(token));
        }
    }

    @Override
    public void registerPrivacyPolicy(ServiceRegisteredEvent event) {
        jdbcClient.saveEndpointsByService(event);
    }
}
