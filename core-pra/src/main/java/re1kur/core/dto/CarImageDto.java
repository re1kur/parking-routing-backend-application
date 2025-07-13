package re1kur.core.dto;

import lombok.Builder;

import java.util.UUID;

@Builder
public record CarImageDto(
        UUID carId,
        String fileId
) {
}
