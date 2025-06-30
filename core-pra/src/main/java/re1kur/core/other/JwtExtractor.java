package re1kur.core.other;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Base64;

public class JwtExtractor {
    private static final ObjectMapper mapper = new ObjectMapper();

    static public String extractSubFromJwt(String header) {
        String token = header.replace("Bearer ", "");
        String[] chunks = token.split("\\.");
        if (chunks.length < 2) {
            throw new IllegalArgumentException("Invalid JWT");
        }
        String payload = new String(Base64.getUrlDecoder().decode(chunks[1]));
        try {
            return mapper.readTree(payload).get("sub").asText();
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse JWT payload", e);
        }
    }
}
