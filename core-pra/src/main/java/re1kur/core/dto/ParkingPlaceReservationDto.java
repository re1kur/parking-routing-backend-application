package re1kur.core.dto;

import lombok.Builder;

import java.time.OffsetDateTime;
import java.util.UUID;

@Builder
public record ParkingPlaceReservationDto(
        UUID id,
        Integer placeNumber,
        UUID occupantUserId,
        OffsetDateTime reservedAt,
        OffsetDateTime endsAt,
        Boolean isPaid
) {
}
