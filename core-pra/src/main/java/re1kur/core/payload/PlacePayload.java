package re1kur.core.payload;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record PlacePayload(
        Integer number,
        @NotNull(message = "The latitude must be defined.")
        BigDecimal latitude,
        @NotNull(message = "The longitude must be defined.")
        BigDecimal longitude
) {
}
