package re1kur.core.other;

import java.time.Instant;

public record PresignedUrl(String url, Instant expiration) {
}
