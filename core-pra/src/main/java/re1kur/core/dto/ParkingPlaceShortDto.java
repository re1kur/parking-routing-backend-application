package re1kur.core.dto;

import lombok.Builder;

@Builder
public record ParkingPlaceShortDto(
        Integer number,
        Float latitude,
        Float longitude
) {
}
