package re1kur.core.payload;

import java.time.OffsetDateTime;

public record ParkingPlaceReservationPayload(
        Integer placeNumber,
        OffsetDateTime reservedAt,
        OffsetDateTime endsAt
) {
}
