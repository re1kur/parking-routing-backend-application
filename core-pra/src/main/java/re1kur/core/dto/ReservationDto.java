package re1kur.core.dto;

import lombok.Builder;

import java.util.UUID;

@Builder
public record ReservationDto(
        UUID id,
        Integer place,
        UUID userId,
        UUID carId,
        Boolean paid
) {
}
