package re1kur.core.dto;

import lombok.Builder;

import java.util.UUID;

@Builder
public record ParkingPlaceFullDto(
        Integer number,
        Float latitude,
        Float longitude,
        Boolean isAvailable,
        UUID occupantCarId,
        ParkingPlaceReservationDto reservation
) {
}
