package re1kur.core.dto;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record ReservationFullDto(
        UUID id,
        Integer place,
        UUID userId,
        UUID carId,
        Boolean paid,
        LocalDateTime reservedAt,
        LocalDateTime startAt,
        LocalDateTime endAt
) {
}
