package re1kur.core.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record LoginRequest(
        @NotBlank(message = "Phone number can not be empty.")
        @Size(min = 10, max = 10, message = "Phone number must be 10 symbols long.")
        String phoneNumber,
        @NotBlank(message = "Verification code can not be empty or contain backspaces.")
        String code
) {
}
