package re1kur.core.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record RegionDto(
        String name,
        List<String> codes
) {
}
