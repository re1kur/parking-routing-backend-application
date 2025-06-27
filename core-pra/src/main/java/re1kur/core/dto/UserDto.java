package re1kur.core.dto;

import lombok.Builder;

@Builder
public record UserDto (
        String id,
        String email,
        String phoneNumber,
        Boolean enabled,
        UserInformationDto info
) {
}