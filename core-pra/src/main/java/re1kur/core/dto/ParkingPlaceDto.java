package re1kur.core.dto;

import lombok.Builder;

@Builder
public record ParkingPlaceDto(
        Integer number,
        Float latitude,
        Float longitude
) {
}
