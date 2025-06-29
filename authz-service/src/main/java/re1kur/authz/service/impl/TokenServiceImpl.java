package re1kur.authz.service.impl;

import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTParser;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import re1kur.authz.entity.RefreshToken;
import re1kur.core.dto.Credentials;
import re1kur.authz.jwt.JwtProvider;
import re1kur.authz.repository.TokenRepository;
import re1kur.authz.service.TokenService;
import re1kur.core.dto.JwtToken;
import re1kur.core.exception.*;

import java.security.interfaces.RSAPublicKey;
import java.text.ParseException;
import java.time.Instant;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {
    private final JwtProvider jwtProvider;
    private final TokenRepository repo;

    @Value("${custom.jwt.publicKeyPath}")
    private String publicKeyPath;

    @Value("${custom.jwt.kidPath}")
    private String kidPath;

    @Override
    public JwtToken refreshToken(String refreshToken) throws ParseException {
        Credentials credentials = verify(refreshToken);

        String sub = credentials.sub();
        RefreshToken token = repo.findById(sub).orElseThrow(() ->
                new TokenNotFoundException("Token for user %s not found.".formatted(credentials)));
        if (!token.getBody().equals(refreshToken)) {
            throw new TokenMismatchException("Refresh token mismatch");
        }

        return jwtProvider.getToken(credentials);
    }

    private Credentials verify(String refreshToken) throws ParseException {
        JWT parsed = JWTParser.parse(refreshToken);
        String sub = parsed.getJWTClaimsSet().getSubject();
        Instant expiration = parsed.getJWTClaimsSet().getExpirationTime().toInstant();
        if (!jwtProvider.verifySignature(parsed)) {
            throw new TokenDidNotPassVerificationException("Token did not pass verification.");
        }
        if (Instant.now().isAfter(expiration)) {
            throw new TokenHasExpiredException("Token has expired. Authenticate again.");
        }
        return Credentials.builder()
                .sub(sub)
                .email((String) parsed.getJWTClaimsSet().getClaim("email"))
                .phone((String) parsed.getJWTClaimsSet().getClaim("phone"))
                .scope((String) parsed.getJWTClaimsSet().getClaim("scope"))
                .build();
    }

    @Override
    public Map<String, Object> getPublicKey() {
        String kid = jwtProvider.readKidFromFile(kidPath);
        RSAPublicKey publicKey = jwtProvider.readPublicKeyFromFile(publicKeyPath);

        RSAKey publicJwk = new RSAKey.Builder(publicKey)
                .keyID(kid)
                .build();
        return Map.of("keys", List.of(publicJwk.toPublicJWK().toJSONObject()));
    }
}
