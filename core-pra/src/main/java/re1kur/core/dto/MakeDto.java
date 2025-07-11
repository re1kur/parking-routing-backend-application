package re1kur.core.dto;

import lombok.Builder;

@Builder
public record MakeDto(
        Integer id,
        String name
) {
}
