package re1kur.core.dto;

import lombok.Builder;

import java.util.UUID;

@Builder
public record CarFullDto(
        UUID carId,
        UUID ownerId,
        String licensePlate,
        String regionCode,
        String regionName,
        Integer makeId,
        String model,
        String color
) {
}
