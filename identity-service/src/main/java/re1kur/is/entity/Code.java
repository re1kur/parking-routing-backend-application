package re1kur.is.entity;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

import java.time.Instant;

@RedisHash("verification_code")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Code {
    @Id
    private String id;
    private String value;
    private Instant issuedAt;
    private Instant expiresAt;

    public boolean isExpired() {
        Instant now = Instant.now();
        return now.isAfter(expiresAt);
    }

    public boolean isMatches(String expected) {
        return value.equals(expected);
    }
}
