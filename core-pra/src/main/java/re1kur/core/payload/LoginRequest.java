package re1kur.core.payload;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record LoginRequest(
        String email,
        String phoneNumber,
        @NotBlank(message = "Verification code can not be empty or contain backspaces.")
        String code
) {
}
