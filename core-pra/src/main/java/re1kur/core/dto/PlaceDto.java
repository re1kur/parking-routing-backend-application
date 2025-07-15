package re1kur.core.dto;

import lombok.Builder;

@Builder
public record PlaceDto(
        Integer number,
        Float latitude,
        Float longitude
) {
}
