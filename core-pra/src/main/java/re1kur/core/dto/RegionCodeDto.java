package re1kur.core.dto;

import lombok.Builder;

@Builder
public record RegionCodeDto(
        Integer regionId,
        String codeValue
) {
}
