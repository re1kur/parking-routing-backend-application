package re1kur.authz.service.impl;

import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import re1kur.authz.authz.ParsedURI;
import re1kur.authz.client.IdentityClient;
import re1kur.authz.client.JdbcClient;
import re1kur.core.dto.Credentials;
import re1kur.authz.jwt.JwtProvider;
import re1kur.authz.service.AuthzService;
import re1kur.core.dto.JwtToken;
import re1kur.core.exception.EndpointNotFoundException;
import re1kur.core.exception.TokenDidNotPassVerificationException;
import re1kur.core.exception.UserDoesNotHavePermissionForEndpoint;
import re1kur.core.payload.LoginRequest;

import java.net.URI;
import java.text.ParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthzServiceImpl implements AuthzService {
    private final IdentityClient idClient;
    private final JwtProvider jwtProvider;
    private final JdbcClient jdbcClient;

    @Value("${jwt.claim-name.roles}")
    private String rolesClaimName;

    @Override
    public JwtToken login(LoginRequest payload) {
        Credentials credentials = idClient.authenticate(payload);
        return jwtProvider.getToken(credentials);
    }

    @Override
    public void authorizeRequest(String token, String uri, String methodType) throws ParseException {
        JWT jwt = SignedJWT.parse(token);
        String subject = jwt.getJWTClaimsSet().getSubject();
        if (!jwtProvider.verifySignature(jwt))
            throw new TokenDidNotPassVerificationException("Token did not pass verification.");

        ParsedURI parsedURI = new ParsedURI(URI.create(uri));
        Optional<List<String>> optionalRoles = jdbcClient.findRolesForEndpoint(parsedURI, methodType);
        if (optionalRoles.isEmpty())
            throw new EndpointNotFoundException("Endpoint %s [%s] not found.".formatted(uri, methodType));

        List<String> allowedRoles = optionalRoles.get();
        if (allowedRoles.isEmpty())
            return;

        String rolesClaim = jwt.getJWTClaimsSet().getStringClaim(rolesClaimName);
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
}
