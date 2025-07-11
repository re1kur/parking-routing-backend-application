package re1kur.authz.service;

import static org.junit.jupiter.api.Assertions.*;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import re1kur.authz.entity.RefreshToken;
import re1kur.authz.repository.TokenRepository;
import re1kur.authz.service.impl.TokenServiceImpl;
import re1kur.core.exception.InvalidTokenException;
import re1kur.core.dto.Credentials;
import re1kur.authz.jwt.JwtProvider;
import re1kur.core.dto.JwtToken;
import re1kur.core.exception.TokenDidNotPassVerificationException;

import java.time.Instant;
import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import re1kur.core.exception.*;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TokenServiceImplTest {

    @InjectMocks
    private TokenServiceImpl service;

    @Mock
    private JwtProvider jwtProvider;

    @Mock
    private TokenRepository repo;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(service, "claimNameRoles", "roles");
    }

    private static String generateValidToken(Date expiresAt, String subject) throws Exception {
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(subject)
                .expirationTime(expiresAt)
                .claim("email", "user@mail.com")
                .claim("phone", "+123456789")
                .claim("roles", "USER")
                .build();
        SignedJWT signedJWT = new SignedJWT(
                new JWSHeader(JWSAlgorithm.HS256),
                claimsSet
        );
        signedJWT.sign(new MACSigner("12345678901234567890123456789012"));
        return signedJWT.serialize();
    }

    @Test
    void refreshToken__ValidFlow__ReturnsJwtToken() throws Exception {
        String subject = "user123";
        String refreshToken = generateValidToken(Date.from(Instant.now().plusSeconds(60)), subject);

        RefreshToken storedToken = new RefreshToken();
        storedToken.setId(subject);
        storedToken.setBody(refreshToken);

        Credentials expectedCreds = Credentials.builder()
                .sub(subject)
                .email("user@mail.com")
                .phone("+123456789")
                .scope("USER")
                .build();

        JwtToken expectedJwt = new JwtToken("access", "refresh", LocalDateTime.now().plusMinutes(5), LocalDateTime.now().plusMinutes(10));

        when(jwtProvider.verifySignature(any(JWT.class))).thenReturn(true);
        when(repo.findById(subject)).thenReturn(Optional.of(storedToken));
        when(jwtProvider.getToken(expectedCreds)).thenReturn(expectedJwt);

        JwtToken result = service.refreshToken(refreshToken);

        assertEquals(expectedJwt, result);
        verify(jwtProvider).verifySignature(any(JWT.class));
        verify(repo).findById(subject);
        verify(jwtProvider).getToken(expectedCreds);
    }

    @Test
    void refreshToken__InvalidSignature__Throws() throws Exception {
        String refreshToken = generateValidToken(Date.from(Instant.now().plusSeconds(60)), "user");

        when(jwtProvider.verifySignature(any(JWT.class))).thenReturn(false);

        assertThrows(TokenDidNotPassVerificationException.class, () -> service.refreshToken(refreshToken));

        verify(jwtProvider).verifySignature(any(JWT.class));
        verifyNoInteractions(repo);
    }

    @Test
    void refreshToken__Expired__Throws() throws Exception {
        String refreshToken = generateValidToken(Date.from(Instant.now().minusSeconds(60)), "user");

        when(jwtProvider.verifySignature(any(JWT.class))).thenReturn(true);

        assertThrows(TokenHasExpiredException.class, () -> service.refreshToken(refreshToken));
    }

    @Test
    void refreshToken__TokenNotFound__Throws() throws Exception {
        String refreshToken = generateValidToken(Date.from(Instant.now().plusSeconds(60)), "user");

        when(jwtProvider.verifySignature(any(JWT.class))).thenReturn(true);
        when(repo.findById("user")).thenReturn(Optional.empty());

        assertThrows(TokenNotFoundException.class, () -> service.refreshToken(refreshToken));
    }

    @Test
    void refreshToken__TokenMismatch__Throws() throws Exception {
        String refreshToken = generateValidToken(Date.from(Instant.now().plusSeconds(60)), "user");

        RefreshToken storedToken = new RefreshToken();
        storedToken.setId("user");
        storedToken.setBody("different-token");

        when(jwtProvider.verifySignature(any(JWT.class))).thenReturn(true);
        when(repo.findById("user")).thenReturn(Optional.of(storedToken));

        assertThrows(TokenMismatchException.class, () -> service.refreshToken(refreshToken));
    }

    @Test
    void refreshToken__ParseException__Throws() {
        String invalidToken = "not-a-jwt";
        assertThrows(InvalidTokenException.class, () -> service.refreshToken(invalidToken));
    }
}
