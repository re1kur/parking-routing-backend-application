package re1kur.core.dto;

import lombok.Builder;

import java.util.UUID;

@Builder
public record CarDto(
        UUID carId,
        UUID ownerId,
        String licensePlate,
        String regionCode,
        String makeName,
        String model,
        UUID titleImageId
) {
}
