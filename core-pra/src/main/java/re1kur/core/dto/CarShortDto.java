package re1kur.core.dto;

import lombok.Builder;

import java.util.UUID;

@Builder
public record CarShortDto(
        UUID id,
        UUID ownerId,
        String licensePlate,
        String regionCode
) {
}
