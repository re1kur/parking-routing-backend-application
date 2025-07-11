package re1kur.core.payload;

import jakarta.validation.constraints.NotBlank;

public record UserInformationPayload(
        @NotBlank
        String firstName,
        @NotBlank
        String lastName,
        String middleName
) {
}
