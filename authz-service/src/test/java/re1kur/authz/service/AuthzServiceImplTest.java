package re1kur.authz.service;

import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;
import re1kur.authz.client.IdentityClient;
import re1kur.authz.client.JdbcClient;
import re1kur.authz.jwt.JwtProvider;
import re1kur.authz.service.impl.AuthzServiceImpl;
import re1kur.core.dto.Credentials;
import re1kur.core.dto.JwtToken;
import re1kur.core.exception.*;
import re1kur.core.payload.LoginRequest;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthzServiceImplTest {

    @InjectMocks
    private AuthzServiceImpl service;

    @Mock
    private IdentityClient idClient;

    @Mock
    private JwtProvider jwtProvider;

    @Mock
    private JdbcClient jdbcClient;

    @BeforeEach
    void setup() {
        ReflectionTestUtils.setField(service, "rolesClaimName", "roles");
    }

    @Test
    void authorizeRequest__ValidRequest__DoesNotThrowException() throws Exception {
        String token = "Bearer valid-token";
        String uri = "/api/data";
        String method = "GET";

        SignedJWT signedJWT = mock(SignedJWT.class);
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject("user1")
                .claim("roles", "ADMIN,USER")
                .build();

        when(jwtProvider.verifySignature(any())).thenReturn(true);
        when(signedJWT.getJWTClaimsSet()).thenReturn(claimsSet);
        when(jdbcClient.findRolesForEndpoint(any(), eq(method)))
                .thenReturn(Optional.of(List.of("ADMIN", "MODERATOR")));

        try (MockedStatic<SignedJWT> staticMock = mockStatic(SignedJWT.class)) {
            staticMock.when(() -> SignedJWT.parse("valid-token")).thenReturn(signedJWT);

            assertDoesNotThrow(() -> service.authorizeRequest(token, uri, method));
        }
    }

    @Test
    void authorizeRequest__InvalidJwtParsing__ThrowsException() {
        String token = "Bearer invalid-token";

        try (MockedStatic<SignedJWT> staticMock = mockStatic(SignedJWT.class)) {
            staticMock.when(() -> SignedJWT.parse("invalid-token")).thenThrow(new ParseException("bad token", 0));

            assertThrows(InvalidTokenException.class,
                    () -> service.authorizeRequest(token, "/api/data", "GET"));
        }
    }

    @Test
    void authorizeRequest__InvalidSignature__ThrowsException() throws Exception {
        String token = "Bearer token";
        SignedJWT signedJWT = mock(SignedJWT.class);
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder().subject("user1").build();

        try (MockedStatic<SignedJWT> staticMock = mockStatic(SignedJWT.class)) {
            staticMock.when(() -> SignedJWT.parse("token")).thenReturn(signedJWT);
            when(signedJWT.getJWTClaimsSet()).thenReturn(claimsSet);
            when(jwtProvider.verifySignature(signedJWT)).thenReturn(false);

            assertThrows(TokenDidNotPassVerificationException.class,
                    () -> service.authorizeRequest(token, "/api/data", "GET"));
        }
    }

    @Test
    void authorizeRequest__EndpointNotFound__ThrowsException() throws Exception {
        String token = "Bearer token";
        SignedJWT signedJWT = mock(SignedJWT.class);
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder().subject("user1").build();

        try (MockedStatic<SignedJWT> staticMock = mockStatic(SignedJWT.class)) {
            staticMock.when(() -> SignedJWT.parse("token")).thenReturn(signedJWT);
            when(signedJWT.getJWTClaimsSet()).thenReturn(claimsSet);
            when(jwtProvider.verifySignature(signedJWT)).thenReturn(true);
            when(jdbcClient.findRolesForEndpoint(any(), eq("GET"))).thenReturn(Optional.empty());

            assertThrows(EndpointNotFoundException.class,
                    () -> service.authorizeRequest(token, "/api/data", "GET"));
        }
    }

    @Test
    void authorizeRequest__NoRolesInToken__ThrowsUserDoesNotHavePermissionForEndpoint() throws Exception {
        String token = "Bearer token";
        SignedJWT signedJWT = mock(SignedJWT.class);
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject("user1")
                .build();

        try (MockedStatic<SignedJWT> staticMock = mockStatic(SignedJWT.class)) {
            staticMock.when(() -> SignedJWT.parse("token")).thenReturn(signedJWT);
            when(signedJWT.getJWTClaimsSet()).thenReturn(claimsSet);
            when(jwtProvider.verifySignature(signedJWT)).thenReturn(true);
            when(jdbcClient.findRolesForEndpoint(any(), eq("GET")))
                    .thenReturn(Optional.of(List.of("ADMIN")));

            assertThrows(UserDoesNotHavePermissionForEndpoint.class,
                    () -> service.authorizeRequest(token, "/api/data", "GET"));
        }
    }

    @Test
    void authorizeRequest__RolesNotAllowed__ThrowsUserDoesNotHavePermissionForEndpoint() throws Exception {
        String token = "Bearer token";
        SignedJWT signedJWT = mock(SignedJWT.class);
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject("user1")
                .claim("roles", "USER")
                .build();

        try (MockedStatic<SignedJWT> staticMock = mockStatic(SignedJWT.class)) {
            staticMock.when(() -> SignedJWT.parse("token")).thenReturn(signedJWT);
            when(signedJWT.getJWTClaimsSet()).thenReturn(claimsSet);
            when(jwtProvider.verifySignature(signedJWT)).thenReturn(true);
            when(jdbcClient.findRolesForEndpoint(any(), eq("GET")))
                    .thenReturn(Optional.of(List.of("ADMIN")));

            assertThrows(UserDoesNotHavePermissionForEndpoint.class,
                    () -> service.authorizeRequest(token, "/api/data", "GET"));
        }
    }

    @Test
    void authorizeRequest__EmptyAllowedRoles__Returns() throws Exception {
        String token = "Bearer token";
        SignedJWT signedJWT = mock(SignedJWT.class);
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject("user1")
                .claim("roles", "USER")
                .build();

        try (MockedStatic<SignedJWT> staticMock = mockStatic(SignedJWT.class)) {
            staticMock.when(() -> SignedJWT.parse("token")).thenReturn(signedJWT);
            when(signedJWT.getJWTClaimsSet()).thenReturn(claimsSet);
            when(jwtProvider.verifySignature(signedJWT)).thenReturn(true);
            when(jdbcClient.findRolesForEndpoint(any(), eq("GET")))
                    .thenReturn(Optional.of(List.of()));

            assertDoesNotThrow(() -> service.authorizeRequest(token, "/api/data", "GET"));
        }
    }

    @Test
    void login__ValidRequest__ReturnsToken() {
        LoginRequest request = new LoginRequest("user", "pass");
        Credentials credentials = Credentials.builder()
                .sub("user")
                .email("user@mail.com")
                .build();
        JwtToken jwtToken = new JwtToken("access", "refresh",
                LocalDateTime.now().plusMinutes(5), LocalDateTime.now().plusMinutes(10));

        when(idClient.authenticate(request)).thenReturn(credentials);
        when(jwtProvider.getToken(credentials)).thenReturn(jwtToken);

        JwtToken result = service.login(request);

        assertEquals(jwtToken, result);
        verify(idClient, times(1)).authenticate(request);
        verify(jwtProvider, times(1)).getToken(credentials);
    }

    @Test
    void login__InvalidCredentials__ThrowsException() {
        LoginRequest request = new LoginRequest("user", "pass");
        when(idClient.authenticate(request))
                .thenThrow(new IdentityServiceAuthenticationException("Unauthorized", HttpStatus.UNAUTHORIZED.value()));

        IdentityServiceAuthenticationException ex = assertThrows(
                IdentityServiceAuthenticationException.class,
                () -> service.login(request)
        );

        assertEquals(401, ex.getHttpStatusCode());
        verify(idClient, times(1)).authenticate(request);
        verify(jwtProvider, never()).getToken(any());
    }
}
