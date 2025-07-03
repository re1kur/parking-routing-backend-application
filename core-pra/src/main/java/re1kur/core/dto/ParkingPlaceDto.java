package re1kur.core.dto;

import lombok.Builder;

import java.util.UUID;

@Builder
public record ParkingPlaceDto(
        Integer number,
        Float latitude,
        Float longitude,
        UUID occupantCarId,
        UUID reservationId,
        Boolean isAvailable
) {
}
