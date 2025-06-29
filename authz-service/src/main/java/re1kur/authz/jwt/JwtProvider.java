package re1kur.authz.jwt;

import com.nimbusds.jwt.JWT;
import re1kur.core.dto.Credentials;
import re1kur.core.dto.JwtToken;

import java.security.interfaces.RSAPublicKey;

public interface JwtProvider {
    JwtToken getToken(Credentials cred);

    boolean verifySignature(JWT refreshToken);

    String readKidFromFile(String path);

    RSAPublicKey readPublicKeyFromFile(String path);

}
