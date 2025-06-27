package re1kur.core.dto;

import lombok.Builder;

@Builder
public record UserInformationDto(
        String firstName,
        String lastName,
        String middleName
) {
}
