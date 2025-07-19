package re1kur.core.dto;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record PlaceDto(
        Integer number,
        BigDecimal latitude,
        BigDecimal longitude
) {
}
